package dev.deepslate.fallacy.common.block

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

interface BlockWithHeat {
    //外延热量：方块对外部环境的影响热量，用于参与热力学引擎的计算影响环境温度
    fun getEpitaxialHeat(state: BlockState, level: Level, pos: BlockPos): UInt

    //内秉热量: 方块自身蕴含的热量，用于方块功能
    fun getIntrinsicHeat(state: BlockState, level: Level, pos: BlockPos): UInt
}