package dev.deepslate.fallacy.thermodynamics.impl

import dev.deepslate.fallacy.Fallacy
import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import net.minecraft.core.BlockPos
import net.minecraft.util.thread.ProcessorMailbox
import net.minecraft.world.level.ChunkPos
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ConcurrentSkipListSet
import java.util.concurrent.Executors

class ChunkScanner(val threadAmount: Int = 3, val engine: EnvironmentThermodynamicsEngine) {
    private val scanQueue: ConcurrentLinkedQueue<Long> = ConcurrentLinkedQueue()

    //经过初始扫描的区块
    private val scannedSet: ConcurrentSkipListSet<Long> = ConcurrentSkipListSet()

    private val mailbox =
        ProcessorMailbox.create(Executors.newFixedThreadPool(threadAmount), "fallacy-thermodynamics-scan")

    fun enqueue(packedPos: Long) {
        if (hasScanned(packedPos)) return
        scanQueue.offer(packedPos)
    }

    fun enqueue(chunkPos: ChunkPos) {
        enqueue(chunkPos.toLong())
    }

    fun markScanned(packedPos: Long) {
        scannedSet.add(packedPos)
    }

    fun markScanned(chunkPos: ChunkPos) {
        markScanned(chunkPos.toLong())
    }

    fun markUnscanned(packedPos: Long) {
        scannedSet.remove(packedPos)
    }

    fun markUnscanned(chunkPos: ChunkPos) {
        markUnscanned(chunkPos.toLong())
    }

    fun hasScanned(packedPos: Long): Boolean {
        return scannedSet.contains(packedPos)
    }

    fun hasScanned(chunkPos: ChunkPos): Boolean {
        return hasScanned(chunkPos.toLong())
    }

    fun unload(packedChunkPos: Long) {
        scannedSet.remove(packedChunkPos)
    }

    fun unload(chunkPos: ChunkPos) {
        unload(chunkPos.toLong())
    }

//    fun scan() {
//        addScanTask()
//    }

    //区块的初始扫描
    private fun addScanTask() = mailbox.tell {
        val scans = LongOpenHashSet()

        for (i in 0..128) {
            val packed = scanQueue.poll() ?: break
            scans.add(packed)
        }

        val iterator = scans.iterator()
        val updateRecord = LongArrayList()

        while (iterator.hasNext()) {
            val packedChunkPos = iterator.nextLong()
            scanChunk(updateRecord, packedChunkPos)
        }
//        doHeatUpdate(checkPackedPositions.toList())
    }

    private fun scanChunk(record: LongArrayList, packedChunkPos: Long, yRange: IntRange? = null) {
        val level = engine.level
        val chunkPos = ChunkPos(packedChunkPos)
        val xRange = 0..15
        val zRange = 0..15
        val yRange = yRange ?: with(level) { minBuildHeight..maxBuildHeight }

        for (x in xRange) for (y in yRange) for (z in zRange) {
            val fullBlockPos = BlockPos(chunkPos.minBlockX + x, y, chunkPos.minBlockZ + z)
            val state = level.getBlockState(fullBlockPos)

            if (engine.hasHeat(state)) record.add(fullBlockPos.asLong())
        }

        markScanned(packedChunkPos)
        Fallacy.Companion.LOGGER.info("chunk ${ChunkPos(packedChunkPos)} scan finished.")
    }
}