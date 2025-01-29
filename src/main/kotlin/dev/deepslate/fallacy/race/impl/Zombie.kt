package dev.deepslate.fallacy.race.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.behavior.Behavior
import dev.deepslate.fallacy.behavior.Behaviors
import dev.deepslate.fallacy.common.data.player.NutritionState
import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import dev.deepslate.fallacy.race.Race
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer

class Zombie : Race {

    companion object {
        val ID = Fallacy.id("zombie")
        val TAGS = listOf(
            Behaviors.UNDEAD, Behaviors.WEAKNESS_IN_SUNLIGHT, Behaviors.WEAKNESS_IN_DAY,
            Behaviors.BURNING_IN_SUNLIGHT
        )
    }

    override val namespacedId: ResourceLocation = ID

    override val attribute: PlayerAttribute =
        PlayerAttribute(health = 40.0, attackDamage = 4.0, strength = 2.0, armor = 4.0)

    override val nutrition: NutritionState = NutritionState.noNeed()

    override fun tick(
        level: ServerLevel,
        player: ServerPlayer,
        position: BlockPos
    ) {
    }

    override fun set(player: ServerPlayer) {
        Behavior.addAll(player, TAGS)
    }

    override fun remove(player: ServerPlayer) {
        Behavior.removeAll(player, TAGS)
    }
}