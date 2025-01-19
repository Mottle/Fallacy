package dev.deepslate.fallacy.thermodynamics.data

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap
import net.minecraft.core.BlockPos
import net.minecraft.world.level.ChunkPos

class HeatProcessQueue {
    private val threadUnsafeLinkedMap = Long2ObjectLinkedOpenHashMap<HeatTask>(2048)

    fun contains(chunkPos: ChunkPos) = synchronized(this) { threadUnsafeLinkedMap.containsKey(chunkPos.toLong()) }

    val empty: Boolean
        get() = synchronized(this) { threadUnsafeLinkedMap.isEmpty() }

    val size: Int
        get() = synchronized(this) { threadUnsafeLinkedMap.size }

    fun enqueueBlockChange(pos: BlockPos) {
        val chunkPos = ChunkPos(pos)
        val packed = chunkPos.toLong()
        val immutablePos = pos.immutable()
        synchronized(this) {
            threadUnsafeLinkedMap.compute(packed) { k, task ->
                if (task == null) return@compute HeatTask(chunkPos, setOf(immutablePos))
                return@compute task.copy(changedPosition = task.changedPosition + immutablePos)
            }
        }
    }

    fun dequeue(): HeatTask? {
        synchronized(this) {
            return threadUnsafeLinkedMap.pollFirstEntry()?.value
        }
    }

    data class HeatTask(
        val chunkPos: ChunkPos,
        val changedPosition: Set<BlockPos>,
    )
}