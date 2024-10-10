package dev.deepslate.fallacy.thermodynamics

import dev.deepslate.fallacy.thermodynamics.data.HeatLayer
import net.minecraft.core.BlockPos

interface ThermodynamicsEngine {

    companion object {
        const val MIN_HEAT = -273

        const val MAX_HEAT = 0xffff + MIN_HEAT

        fun convert(heat: Int): UInt = (heat + MIN_HEAT).toUInt().coerceIn(0u, HeatLayer.MAX_HEAT)
    }

    fun getEpitaxialHeat(pos: BlockPos): Int

    fun getIntrinsicHeat(pos: BlockPos): Int

    fun checkBlock(pos: BlockPos)
}