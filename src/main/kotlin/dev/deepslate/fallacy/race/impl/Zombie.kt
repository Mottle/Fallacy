package dev.deepslate.fallacy.race.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import dev.deepslate.fallacy.race.Race
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer

class Zombie : Race {

    companion object {
        val FALLACY = Fallacy.id("zombie")
    }

    override val namespacedId: ResourceLocation = FALLACY

    override val attribute: PlayerAttribute = PlayerAttribute(health = 40f, attackDamage = 4f)

    override fun tick(
        level: ServerLevel,
        player: ServerPlayer,
        position: BlockPos
    ) {
    }

    override fun set(player: ServerPlayer) {
    }
}