package dev.deepslate.fallacy.race.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import dev.deepslate.fallacy.race.Race
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer

class Elf : Race {

    companion object {
        val ID = Fallacy.id("elf")
    }

    override val namespacedId: ResourceLocation = ID
    override val attribute: PlayerAttribute = PlayerAttribute(moveSpeed = 0.12, magicResistance = 25.0, strength = 1.0)

    override fun tick(
        level: ServerLevel,
        player: ServerPlayer,
        position: BlockPos
    ) {
    }

    override fun set(player: ServerPlayer) {
    }

    override fun remove(player: ServerPlayer) {
    }
}