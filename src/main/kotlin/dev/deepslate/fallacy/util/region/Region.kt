package dev.deepslate.fallacy.util.region

import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource

sealed class Region {
    abstract fun isIn(x: Int, y: Int, z: Int): Boolean

    fun isIn(pos: BlockPos): Boolean = isIn(pos.x, pos.y, pos.z)

    abstract fun random(source: RandomSource): Triple<Int, Int, Int>

    fun randomPos(source: RandomSource): BlockPos = random(source).let { BlockPos(it.first, it.second, it.third) }
}