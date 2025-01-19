package dev.deepslate.fallacy.common.block

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

interface BlockWithThermalConductivity {
    fun getThermalConductivity(state: BlockState, level: Level, pos: BlockPos): Float
}