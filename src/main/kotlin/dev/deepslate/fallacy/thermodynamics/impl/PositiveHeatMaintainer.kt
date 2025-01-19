package dev.deepslate.fallacy.thermodynamics.impl

import dev.deepslate.fallacy.thermodynamics.HeatMaintainer
import dev.deepslate.fallacy.thermodynamics.HeatStorageCache
import dev.deepslate.fallacy.thermodynamics.ThermodynamicsEngine
import dev.deepslate.fallacy.thermodynamics.data.HeatStorage
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.ChunkPos
import kotlin.math.max

class PositiveHeatMaintainer(engine: EnvironmentThermodynamicsEngine) : HeatMaintainer(engine) {

    override fun query(chunkPos: ChunkPos, cache: HeatStorageCache): HeatStorage = cache.queryPositive(chunkPos)

    override fun getHeat(pos: BlockPos): Int {
        val packed = ChunkPos.asLong(pos)
        val storage = storageCache[packed]!!
        val nibbleIndex = (pos.y - level.minBuildHeight) / 16
        val nibble = storage[nibbleIndex]
        return nibble?.getWriteable(pos.x, pos.y, pos.z) ?: ThermodynamicsEngine.MIN_HEAT
    }

    override fun setHeat(pos: BlockPos, heat: Int) {
        val packed = ChunkPos.asLong(pos)
        val storage = storageCache[packed]!!
        val nibbleIndex = (pos.y - level.minBuildHeight) / 16
        val nibble = storage.getOrInitEmpty(nibbleIndex)
        nibble.set(pos.x, pos.y, pos.z, heat)
        markChangedChunk.add(packed)
    }

    override fun checkBlock(pos: BlockPos) {
        val currentHeat = getHeat(pos)
        val state = getBlockState(pos)
        val emittedHeat = if (ThermodynamicsEngine.isHeatSource(state)) ThermodynamicsEngine.getEpitaxialHeat(
            state,
            level,
            pos
        ) else ThermodynamicsEngine.MIN_HEAT

        if (emittedHeat > ThermodynamicsEngine.MIN_BIOME_HEAT) setHeat(pos, emittedHeat)
        else setHeat(pos, ThermodynamicsEngine.MIN_HEAT)
        if (emittedHeat != ThermodynamicsEngine.MIN_HEAT) increasedQueue.enqueue(
            PropagateTask(
                pos,
                emittedHeat,
                TaskType.NORM
            )
        )

        decreasedQueue.enqueue(PropagateTask(pos, currentHeat, TaskType.NORM))
    }

    override fun performIncrease() {

        while (!increasedQueue.isEmpty) {
            val (pos, propagatedHeat, type) = increasedQueue.dequeue()

            if (type == TaskType.RECHECKED && getHeat(pos) != propagatedHeat) continue
            if (type == TaskType.WRITE) setHeat(pos, propagatedHeat)

            for (direct in Direction.entries) {
                val currentPos = pos.relative(direct)
                val currentHeat = getHeat(currentPos)
                val state = getBlockState(currentPos)
                val thermalConductivity = ThermodynamicsEngine.getThermalConductivity(state, level, currentPos)
                val nextHeat = (decay(propagatedHeat) * thermalConductivity).toInt()

                //低于最低可能的环境温度则不继续传播
                if (nextHeat <= ThermodynamicsEngine.MIN_BIOME_HEAT) continue
                if (nextHeat > currentHeat) setHeat(currentPos, nextHeat) else continue

                increasedQueue.enqueue(PropagateTask(currentPos, nextHeat, TaskType.NORM))
            }
        }
    }

    override fun performDecrease() {
        while (!decreasedQueue.isEmpty) {
            val (pos, propagatedHeat) = decreasedQueue.dequeue()
            for (direct in Direction.entries) {
                val currentPos = pos.relative(direct)
                val currentHeat = getHeat(currentPos)

                //当前温度已经小于最低温度，不会再继续传播
                if (currentHeat <= ThermodynamicsEngine.MIN_BIOME_HEAT) continue

                val state = getBlockState(currentPos)
                val thermalConductivity = ThermodynamicsEngine.getThermalConductivity(state, level, currentPos)
                val nextHeat = (decay(propagatedHeat) * thermalConductivity).toInt()

                if (currentHeat > nextHeat) {
                    increasedQueue.enqueue(PropagateTask(currentPos, currentHeat, TaskType.RECHECKED))
                    continue
                }

                if (ThermodynamicsEngine.isHeatSource(state)) {
                    val emittedHeat = ThermodynamicsEngine.getEpitaxialHeat(state, level, currentPos)
                    increasedQueue.enqueue(PropagateTask(currentPos, emittedHeat, TaskType.WRITE))
                }

                setHeat(currentPos, ThermodynamicsEngine.MIN_HEAT)
                decreasedQueue.enqueue(PropagateTask(currentPos, nextHeat, TaskType.NORM))
            }
        }

        performIncrease()
    }

    fun decay(heat: Int): Float = heat.let {
        val floatHeat = heat.toFloat()
        floatHeat - max(30f, floatHeat * (0.05f + (floatHeat / (2.25f * 8196f))))
    }
}