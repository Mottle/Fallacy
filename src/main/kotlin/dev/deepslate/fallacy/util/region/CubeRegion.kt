package dev.deepslate.fallacy.util.region

import net.minecraft.util.RandomSource

data class CubeRegion(val xStart: Int, val yStart: Int, val zStart: Int, val xEnd: Int, val yEnd: Int, val zEnd: Int) :
    Region() {
    override fun isIn(x: Int, y: Int, z: Int): Boolean = x in xStart..xEnd && y in yStart..yEnd && z in zStart..zEnd

    override fun random(source: RandomSource): Triple<Int, Int, Int> {
        val randomX = source.nextInt(xStart, xEnd + 1)
        val randomY = source.nextInt(yStart, yEnd + 1)
        val randomZ = source.nextInt(zStart, zEnd + 1)
        return Triple(randomX, randomY, randomZ)
    }
}