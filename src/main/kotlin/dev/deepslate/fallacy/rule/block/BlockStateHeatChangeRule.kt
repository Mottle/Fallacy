package dev.deepslate.fallacy.rule.block

import dev.deepslate.fallacy.thermodynamics.impl.EnvironmentThermodynamicsEngine
import dev.deepslate.fallacy.thermodynamics.VanillaHeat
import dev.deepslate.fallacy.util.getEpitaxialHeat
import dev.deepslate.fallacy.util.hasHeat
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

object BlockStateHeatChangeRule {
    fun rule(new: BlockState, old: BlockState, level: Level, pos: BlockPos) {
        if (level.isClientSide) return

        val engine = EnvironmentThermodynamicsEngine.getEngine(level as ServerLevel)
        engine.updateHeat(old, new, pos)
    }
}