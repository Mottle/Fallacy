package dev.deepslate.fallacy.thermodynamics.impl

import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.thermodynamics.ThermodynamicsEngine
import dev.deepslate.fallacy.thermodynamics.data.ChunkHeat
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.thread.ProcessorMailbox
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.LiquidBlock
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import kotlin.math.absoluteValue
import kotlin.math.pow

class HeatMaintainer(val engine: EnvironmentThermodynamicsEngine) {
    sealed interface MaintainTask {
        val pos: BlockPos

        val heat: Float

        data class Increase(override val pos: BlockPos, override val heat: Float) : MaintainTask

        data class Decrease(override val pos: BlockPos, override val heat: Float) : MaintainTask
    }

    private data class ComposedHeat(val blockHeat: Float, val biomeHeat: Float) {
        val final = blockHeat + biomeHeat
    }

    //只能由HeatCacheMaintainer消费
    private val maintainQueue: ConcurrentLinkedQueue<MaintainTask> = ConcurrentLinkedQueue()

    private var processed: List<MaintainTask> = emptyList()

    private val mailbox =
        ProcessorMailbox.create(Executors.newSingleThreadExecutor(), "fallacy-thermodynamics-maintainer")

    fun enqueueIncrease(pos: BlockPos, heat: Float) {
        maintainQueue.add(MaintainTask.Increase(pos, heat))
    }

    fun enqueueDecrease(pos: BlockPos, heat: Float) {
        maintainQueue.add(MaintainTask.Decrease(pos, heat))
    }

    fun load(list: List<MaintainTask>) {
        maintainQueue.addAll(list)
        tryWakeUp()
    }

    fun addTask(runnable: Runnable) = mailbox.tell(runnable)

    val level: Level
        get() = engine.level

    fun forceStop() = mailbox.close()

    fun getSaved(): SavedHeatTask {
        val remainder = processed + maintainQueue
        val saved = SavedHeatTask(remainder.toMutableList())
        return saved
    }

    private fun runHeatUpdate() {
        if (maintainQueue.isEmpty()) return
        addTask {
            var count = 0
            val processedTask = mutableListOf<MaintainTask>()

            while (!maintainQueue.isEmpty() && count < 512) {
                val maintainTask = maintainQueue.poll()
                processedTask.add(maintainTask)
                ++count
            }

            processed = processedTask

            processedTask.forEach {
                update(it)
            }

            processed = emptyList()
        }
    }

    fun tryWakeUp() {
        if(maintainQueue.isEmpty()) runHeatUpdate()
        else if(maintainQueue.size <= 32 * 1024) runHeatUpdate()
    }

    private fun update(task: MaintainTask) {
        val startPos = task.pos
        val sourceHeat = task.heat

        val usedSet = LongOpenHashSet(32 * 32 * 16)
        val bfsQueue = ObjectArrayFIFOQueue<BlockPos>(4098)
        val heatMap = Object2ObjectLinkedOpenHashMap<BlockPos, ComposedHeat>()
        val increased = task is MaintainTask.Increase

        bfsQueue.enqueue(startPos)
        usedSet.add(startPos.asLong())
        heatMap.put(startPos, ComposedHeat(sourceHeat, 0f))

        while (!bfsQueue.isEmpty) {
            val pos = bfsQueue.dequeue()

            if (pos != startPos) {
                val heat = findHighestHeat(pos, heatMap).blockHeat
                val biomeHeat = engine.getBiomeHeat(pos)
                val dis = startPos.distManhattan(pos)
                val (decayedHeat, decayedBiomeHeat) = decayHeat(heat, biomeHeat, dis)
                val nextHeat = decayedHeat * getThermalConductivity(pos)

                if ((biomeHeat - nextHeat).absoluteValue < 15f) continue
                if(sourceHeat > biomeHeat && nextHeat < biomeHeat) continue
                if(sourceHeat < biomeHeat && nextHeat > biomeHeat) continue

                heatMap[pos] = ComposedHeat(nextHeat, decayedBiomeHeat)
            }

            Direction.entries.forEach { direction ->
                val nextPos = pos.relative(direction)
                val packedNextPos = nextPos.asLong()
                val dis = nextPos.distManhattan(startPos)

                if (packedNextPos in usedSet) return@forEach
                if (dis > 15) return@forEach //the max bfs steps

                bfsQueue.enqueue(nextPos)
                usedSet.add(packedNextPos)
            }
        }

        applyHeat(heatMap, level as ServerLevel, increased)
    }

    private fun findHighestHeat(
        pos: BlockPos,
        heatMap: Object2ObjectLinkedOpenHashMap<BlockPos, ComposedHeat>
    ): ComposedHeat = Direction.entries.map { pos.relative(it) }
        .filter {heatMap.contains(it) }.map { heatMap.getValue(it) }
        .maxWith { a, b -> a.blockHeat.absoluteValue.compareTo(b.blockHeat.absoluteValue) }

    //返回衰减后的块温度影响和环境温度
    private fun decayHeat(heat: Float, biomeHeat: Float, dis: Int): Pair<Float, Float> {
//        val rate = 1f / (dis.toFloat() * 1.1f.pow(dis - 1))
        val rate = 0.3f + 0.6f * (15 - dis).toFloat() / 15f
        return heat * rate to biomeHeat * (1 - rate)
    }

    private fun getThermalConductivity(pos: BlockPos): Float {
        val state = level.getBlockState(pos)
        if (state.isAir) return 1f
        if (state.block is LiquidBlock) return 0.9f
        if (state.isCollisionShapeFullBlock(level, pos)) return 0.4f
        return 0.75f
    }

    private fun applyHeat(
        heatMap: Object2ObjectLinkedOpenHashMap<BlockPos, ComposedHeat>,
        level: ServerLevel,
        increased: Boolean
    ) {
        val chunkHeatMap = Object2ObjectLinkedOpenHashMap<ChunkPos, ChunkHeat>()
        heatMap.forEach { (blockPos, composed) ->
            val chunkPos = ChunkPos(blockPos)
            if (!chunkHeatMap.contains(chunkPos)) chunkHeatMap[chunkPos] = ThermodynamicsEngine.initAndGetChunkHeat(chunkPos, level)
            if (increased) chunkHeatMap[chunkPos]!!.add(blockPos, composed.final, level)
            else chunkHeatMap[chunkPos]!!.remove(blockPos, composed.final, level)
        }

        chunkHeatMap.forEach { (chunkPos, data) ->
            ThermodynamicsEngine.setChunkHeat(data, chunkPos, level)
        }
    }
}