package dev.deepslate.fallacy.race

import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer

interface Race {
    val namespacedId: ResourceLocation

    val attribute: PlayerAttribute

    fun tick(level: ServerLevel, player: ServerPlayer, position: BlockPos)

    fun set(player: ServerPlayer)
}