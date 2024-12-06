package dev.deepslate.fallacy.util

import dev.deepslate.fallacy.common.block.BlockWithHeat
import dev.deepslate.fallacy.thermodynamics.ThermodynamicsEngine
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

fun BlockState.hasHeat(): Boolean = this.block is BlockWithHeat

fun BlockState.getEpitaxialHeat(level: Level, pos: BlockPos) =
    if (!hasHeat()) ThermodynamicsEngine.fromFreezingPoint(0) else (block as BlockWithHeat).getEpitaxialHeat(
        this,
        level,
        pos
    )

fun BlockState.getIntrinsicHeat(level: Level, pos: BlockPos) =
    if (!hasHeat()) ThermodynamicsEngine.fromFreezingPoint(0) else (block as BlockWithHeat).getIntrinsicHeat(
        this,
        level,
        pos
    )