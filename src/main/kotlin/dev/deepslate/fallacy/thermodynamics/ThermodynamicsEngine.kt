package dev.deepslate.fallacy.thermodynamics

import dev.deepslate.fallacy.thermodynamics.data.HeatLayer
import net.minecraft.core.BlockPos

interface ThermodynamicsEngine {
    fun getHeat(pos: BlockPos): UInt

    fun doCheck(pos: BlockPos)

    companion object {
        private val FREEZING_POINT = HeatLayer.FREEZING_POINT.toInt()

        fun fromFreezingPoint(heat: Int): UInt = (heat + FREEZING_POINT).toUInt()

        fun isUnchecked(heat: UInt): Boolean = heat == HeatLayer.UNCHECKED_HEAT
    }
}