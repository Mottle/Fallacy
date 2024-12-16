package dev.deepslate.fallacy.thermodynamics.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.thermodynamics.VanillaHeat
import dev.deepslate.fallacy.thermodynamics.data.HeatUpdateCache
import dev.deepslate.fallacy.thermodynamics.data.LayerStack
import dev.deepslate.fallacy.util.getEpitaxialHeat
import dev.deepslate.fallacy.util.hasHeat
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.thread.ProcessorMailbox
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.block.state.BlockState
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import kotlin.math.absoluteValue
import kotlin.math.pow

class HeatCacheMaintainer(val engine: EnvironmentThermodynamicsEngine) {
    private sealed interface MaintainTask {
        val pos: BlockPos

        data class Increase(override val pos: BlockPos) : MaintainTask
        data class Decrease(override val pos: BlockPos) : MaintainTask
    }

    //只能由HeatCacheMaintainer消费
    private val maintainQueue: ConcurrentLinkedQueue<MaintainTask> = ConcurrentLinkedQueue()

    private val chunkHeatCache: ConcurrentHashMap<Long, HeatUpdateCache> = ConcurrentHashMap()

    private val mailbox =
        ProcessorMailbox.create(Executors.newSingleThreadExecutor(), "fallacy-thermodynamics-maintain")

    fun enqueueIncrease(pos: BlockPos) {
        maintainQueue.add(MaintainTask.Increase(pos))
    }

    fun enqueueDecrease(pos: BlockPos) {
        maintainQueue.add(MaintainTask.Decrease(pos))
    }

    fun addTask(runnable: Runnable) = mailbox.tell(runnable)

    fun unload(packedChunkPos: Long) {
        chunkHeatCache.remove(packedChunkPos)
    }

    val level: Level
        get() = engine.level

    private fun runHeatUpdate() {
        if (maintainQueue.isEmpty()) return
        addTask {
            val relativeChunks = LongOpenHashSet()
            var count = 0

            while (!maintainQueue.isEmpty() && count < 512) {
                val maintainTask = maintainQueue.poll()

                update(maintainTask)
                relativeChunks.add(ChunkPos.asLong(maintainTask.pos))
                ++count
            }

            relativeChunks.forEach { packedPos ->
                applyHeatTarget(packedPos)
                Fallacy.Companion.LOGGER.info("apply chunk {}", ChunkPos(packedPos))
            }
        }
    }

    private fun update(task: MaintainTask) {
        val usedSet = LongOpenHashSet(1024)
        val queue = LongArrayFIFOQueue(1024)
        val startPos = task.pos
        val state = level.getBlockState(startPos)
        val increased = task is MaintainTask.Increase

        if (!state.hasHeat() && !VanillaHeat.hasHeat(state)) return

        val startHeat = getEpitaxialHeat(state, level, startPos)
        val packedPos = startPos.asLong()

        queue.enqueue(packedPos)
        queue.enqueue(startHeat.toLong())
        usedSet.add(packedPos)

        while (!queue.isEmpty) {
            val packed = queue.dequeueLong()
            val pos = BlockPos.of(packed)
            val posHeat = queue.dequeueLong().toUInt()

            Direction.entries.forEach { direction ->
                val nextPos = pos.relative(direction)
                val packedNextPos = nextPos.asLong()
                val dis = nextPos.distManhattan(startPos)
                val biomeHeat = engine.getBiomeHeat(nextPos)

                if (packedNextPos in usedSet) return@forEach
                if (dis > 15) return@forEach //the max bfs steps

                val nextHeat = (decayHeat(posHeat, biomeHeat, dis) * getThermalConductivity(nextPos)).toUInt()

                if ((nextHeat.toInt() - startHeat.toInt()).absoluteValue < 5) return@forEach

                updateHeatTarget(nextPos, nextHeat, increased)
                queue.enqueue(packedNextPos)
                queue.enqueue(nextHeat.toLong())
                usedSet.add(packedNextPos)
            }
        }
    }

    private fun getEpitaxialHeat(state: BlockState, level: Level, pos: BlockPos): UInt {
        if (state.hasHeat()) return state.getEpitaxialHeat(level, pos)
        return VanillaHeat.getHeat(state)
    }

    private fun decayHeat(heat: UInt, envHeat: UInt, dis: Int): Float {
        val rate = 1f / (dis.toFloat() * 1.2f.pow(dis - 1))
        return heat.toFloat() * rate + envHeat.toFloat() * (1f - rate)
    }

    private fun getThermalConductivity(pos: BlockPos): Float {
        val state = level.getBlockState(pos)
        if (state.isAir) return 1f
        if (state.block is LiquidBlock) return 0.9f
        if (state.isCollisionShapeFullBlock(level, pos)) return 0.4f
        return 0.75f
    }

    private fun updateHeatTarget(pos: BlockPos, heat: UInt, increased: Boolean) {
        val packedChunkPos = ChunkPos.asLong(pos)
        val target = chunkHeatCache.computeIfAbsent(packedChunkPos) { HeatUpdateCache(level) }

        target[pos] = if (increased) target[pos].plus(heat) else target[pos].minus(heat)
    }

    private fun applyHeatTarget(packedChunkPos: Long) {
        val chunkPos = ChunkPos(packedChunkPos)
        val target = chunkHeatCache.computeIfAbsent(packedChunkPos) { HeatUpdateCache(level) }
        val layerStack = generateLayerStack(packedChunkPos, target)
        val chunk = level.getChunk(chunkPos.x, chunkPos.z)

        ////
        synchronized(chunk) {
            chunk.setData(FallacyAttachments.CHUNK_HEAT, layerStack)
            chunk.isUnsaved = true
        }
    }

    private fun generateLayerStack(packedChunkPos: Long, data: HeatUpdateCache): LayerStack {
        val chunkPos = ChunkPos(packedChunkPos)
        val lu = ChunkPos(chunkPos.x - 1, chunkPos.z + 1).toLong()
        val u = ChunkPos(chunkPos.x, chunkPos.z + 1).toLong()
        val ru = ChunkPos(chunkPos.x + 1, chunkPos.z + 1).toLong()
        val l = ChunkPos(chunkPos.x - 1, chunkPos.z).toLong()
        val r = ChunkPos(chunkPos.x + 1, chunkPos.z).toLong()
        val ld = ChunkPos(chunkPos.x - 1, chunkPos.z - 1).toLong()
        val d = ChunkPos(chunkPos.x, chunkPos.z - 1).toLong()
        val rd = ChunkPos(chunkPos.x + 1, chunkPos.z - 1).toLong()

        val luCache = chunkHeatCache[lu]
        val uCache = chunkHeatCache[u]
        val ruCache = chunkHeatCache[ru]
        val lCache = chunkHeatCache[l]
        val rCache = chunkHeatCache[r]
        val ldCache = chunkHeatCache[ld]
        val dCache = chunkHeatCache[d]
        val rdCache = chunkHeatCache[rd]

        return data.toLayerStack(luCache, uCache, ruCache, lCache, rCache, ldCache, dCache, rdCache)
    }
}