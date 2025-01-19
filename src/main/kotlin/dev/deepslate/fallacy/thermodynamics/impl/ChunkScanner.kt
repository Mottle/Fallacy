package dev.deepslate.fallacy.thermodynamics.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.thermodynamics.ThermodynamicsEngine
import it.unimi.dsi.fastutil.longs.LongArrayList
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import net.minecraft.core.BlockPos
import net.minecraft.util.thread.ProcessorMailbox
import net.minecraft.world.level.ChunkPos
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors

class ChunkScanner(val threadAmount: Int = 3, val engine: EnvironmentThermodynamicsEngine) {
    private val scanQueue: ConcurrentLinkedQueue<Long> = ConcurrentLinkedQueue()

    private val mailbox =
        ProcessorMailbox.create(Executors.newFixedThreadPool(threadAmount), "fallacy-thermodynamics-scan")

    fun enqueue(chunkPos: ChunkPos) {
        if (hasScanned(chunkPos)) return
        scanQueue.offer(chunkPos.toLong())
        addScanTask()
    }

    fun forceEnqueue(chunkPos: ChunkPos) {
        scanQueue.offer(chunkPos.toLong())
        addScanTask()
    }

    fun hasScanned(chunkPos: ChunkPos): Boolean =
        engine.level.getChunk(chunkPos.x, chunkPos.z).getData(FallacyAttachments.CHUNK_HEAT_SCANNED)

    fun markScanned(chunkPos: ChunkPos) {
        engine.level.getChunk(chunkPos.x, chunkPos.z).setData(FallacyAttachments.CHUNK_HEAT_SCANNED, true)
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

            if (ThermodynamicsEngine.isHeatSource(state)) record.add(fullBlockPos.asLong())
        }

        Fallacy.Companion.LOGGER.info("chunk ${ChunkPos(packedChunkPos)} scan finished.")
        markScanned(chunkPos)
    }
}