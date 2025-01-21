package dev.deepslate.fallacy.thermodynamics.impl

import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.thermodynamics.ThermodynamicsEngine
import net.minecraft.core.BlockPos
import net.minecraft.util.thread.ProcessorMailbox
import net.minecraft.world.level.chunk.ChunkAccess
import java.util.concurrent.Executors

class ChunkScanner(val threadAmount: Int = 3, val engine: EnvironmentThermodynamicsEngine) {

    private val mailbox =
        ProcessorMailbox.create(Executors.newFixedThreadPool(threadAmount), "fallacy-thermodynamics-scan")

    fun enqueue(chunk: ChunkAccess) {
        if (hasScanned(chunk)) return
        forceEnqueue(chunk)
    }

    fun forceEnqueue(chunk: ChunkAccess) {
        mailbox.tell {
//            val d1 = System.currentTimeMillis()
            scanSources(chunk)
//            val d2 = System.currentTimeMillis()
//            Fallacy.LOGGER.info("Scanned chunk ${chunk.pos} in ${d2 - d1}ms")
        }
    }

    fun hasScanned(chunk: ChunkAccess): Boolean = chunk.getData(FallacyAttachments.CHUNK_HEAT_SCANNED)

    fun markScanned(chunk: ChunkAccess) {
        chunk.setData(FallacyAttachments.CHUNK_HEAT_SCANNED, true)
    }


    private fun scanSources(chunk: ChunkAccess) {
        val sections = chunk.sections
        val startPos = chunk.pos.worldPosition

        for (sectionIdx in 0 until sections.size) {
            val section = sections[sectionIdx] ?: continue
            val sectionY = engine.level.minBuildHeight + sectionIdx * 16

            if (section.hasOnlyAir()) continue
            if (!section.maybeHas { ThermodynamicsEngine.isHeatSource(it) }) continue

            val states = section.states

            for (x in 0..15) for (z in 0..15) for (y in 0..15) {
                val state = states[x, y, z]
                if (!ThermodynamicsEngine.isHeatSource(state)) continue

                val currentPos = BlockPos(startPos.x + x, sectionY + y, startPos.z + z)

                engine.checkBlock(currentPos)
            }
        }

        markScanned(chunk)
    }
}