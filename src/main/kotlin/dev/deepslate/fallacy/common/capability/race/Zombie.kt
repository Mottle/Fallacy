package dev.deepslate.fallacy.common.capability.race

import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import net.minecraft.tags.EntityTypeTags
import net.minecraft.world.entity.player.Player

class Zombie(val player: Player) : IRace {

    companion object {
        val ATTRIBUTE = PlayerAttribute(health = 40f, attackDamage = 4f)
    }

    override val defaultAttribute: PlayerAttribute = ATTRIBUTE

    override fun tick() {}

    override fun apply() {
        player.addTag(EntityTypeTags.UNDEAD.location.path)
    }
}