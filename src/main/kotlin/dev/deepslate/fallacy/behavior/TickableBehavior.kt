package dev.deepslate.fallacy.behavior

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer

interface TickableBehavior : Behavior {
    val interval: Int

    fun tick(level: ServerLevel, player: ServerPlayer, position: BlockPos)
}