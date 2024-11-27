package dev.deepslate.fallacy.thermodynamics

import dev.deepslate.fallacy.thermodynamics.data.Temperature
import net.minecraft.core.BlockPos

interface ThermodynamicsEngine {
    //外延热量
    fun getEpitaxialTemperature(pos: BlockPos): Temperature

    //内秉热量
    fun getIntrinsicTemperature(pos: BlockPos): Temperature

    fun doCheck(pos: BlockPos)
}