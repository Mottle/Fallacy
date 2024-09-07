package dev.deepslate.fallacy.race.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import dev.deepslate.fallacy.race.Race
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.EntityTypeTags

class Zombie : Race {
    override val namespacedId: ResourceLocation = Fallacy.id("Zombie")

    override val attribute: PlayerAttribute = PlayerAttribute(health = 40f, attackDamage = 4f)

    override fun tick(
        level: ServerLevel,
        player: ServerPlayer,
        position: BlockPos
    ) {
    }

    override fun set(player: ServerPlayer) {
        player.addTag(EntityTypeTags.UNDEAD.location.path)
    }
}