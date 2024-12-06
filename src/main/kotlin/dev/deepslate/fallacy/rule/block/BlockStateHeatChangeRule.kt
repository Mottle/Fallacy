package dev.deepslate.fallacy.rule.block

import dev.deepslate.fallacy.thermodynamics.EnvironmentThermodynamicsEngine
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

        val oldHeatExisted = old.hasHeat() || VanillaHeat.hasHeat(old)
        val newHeatExisted = new.hasHeat() || VanillaHeat.hasHeat(new)

        if (!oldHeatExisted && !newHeatExisted) return

        val engine = EnvironmentThermodynamicsEngine.getEnvironmentEngineOrNull(level as ServerLevel) ?: return

        if (!engine.hasLoaded(ChunkPos(pos))) return
        if (oldHeatExisted && newHeatExisted) {
            val oldHeat = if (old.hasHeat()) old.getEpitaxialHeat(level, pos) else VanillaHeat.getHeat(old)
            val newHeat = if (new.hasHeat()) new.getEpitaxialHeat(level, pos) else VanillaHeat.getHeat(new)

            if (oldHeat != newHeat) engine.scanChunk(ChunkPos(pos))
        } else {
            engine.scanChunk(ChunkPos(pos))
        }
    }
}