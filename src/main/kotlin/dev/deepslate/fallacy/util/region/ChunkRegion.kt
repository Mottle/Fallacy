package dev.deepslate.fallacy.util.region

import net.minecraft.util.RandomSource
import net.minecraft.world.level.ChunkPos

class ChunkRegion(val chunkStart: ChunkPos, val chunkEnd: ChunkPos) : Region() {
    override fun isIn(x: Int, y: Int, z: Int): Boolean =
        x in chunkStart.minBlockX..chunkEnd.maxBlockX && z in chunkStart.minBlockZ..chunkEnd.maxBlockZ

    private val xStart = chunkStart.minBlockX

    private val xEnd = chunkStart.maxBlockX

    private val zStart = chunkStart.minBlockZ

    private val zEnd = chunkStart.maxBlockZ

    override fun random(source: RandomSource): Triple<Int, Int, Int> {
        val randomX = source.nextInt(xStart, xEnd + 1)
        val randomZ = source.nextInt(zStart, zEnd + 1)
        val randomY = source.nextInt(-64, 321)

        return Triple(randomX, randomY, randomZ)
    }
}