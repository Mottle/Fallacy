package dev.deepslate.fallacy.rule.block

import dev.deepslate.fallacy.thermodynamics.ThermodynamicsEngine
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

object BlockStateHeatChangeRule {
    fun rule(from: BlockState, to: BlockState, level: Level, pos: BlockPos) {
        if (!ThermodynamicsEngine.hasDifferentHeatProperties(from, to, level, pos)) return
        if (level.isClientSide) return

        val serverLevel = level as ServerLevel
        val engine = ThermodynamicsEngine.getEngine(serverLevel)

        engine.checkBlock(pos)
    }
}