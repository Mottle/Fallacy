package dev.deepslate.fallacy.thermodynamics

import dev.deepslate.fallacy.thermodynamics.data.HeatLayer
import net.minecraft.core.BlockPos

interface ThermodynamicsEngine {
    //外延热量：对应位置的方块对其他方块的影响
    fun getEpitaxialHeat(pos: BlockPos): UInt

    //内秉热量: 为对应位置
    fun getIntrinsicHeat(pos: BlockPos): UInt

    fun doCheck(pos: BlockPos)

    companion object {
        private val FREEZING_POINT = HeatLayer.FREEZING_POINT.toInt()

        fun fromFreezingPoint(heat: Int): UInt = (heat + FREEZING_POINT).toUInt()
    }
}