package dev.deepslate.fallacy.race.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.BehaviorTags
import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import dev.deepslate.fallacy.race.Race
import dev.deepslate.fallacy.race.Undead
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer

class Zombie : Race, Undead {

    companion object {
        val ID = Fallacy.id("zombie")
    }

    override val namespacedId: ResourceLocation = ID

    override val attribute: PlayerAttribute = PlayerAttribute(health = 40.0, attackDamage = 4.0)

    override fun tick(
        level: ServerLevel,
        player: ServerPlayer,
        position: BlockPos
    ) {
    }

    override fun set(player: ServerPlayer) {
        attribute.set(player)
        BehaviorTags.set(
            player, BehaviorTags.UNDEAD, BehaviorTags.WEAKNESS2_IN_SUNLIGHT, BehaviorTags.WEAKNESS_IN_DAY,
            BehaviorTags.BURNING_IN_SUNLIGHT
        )
    }
}