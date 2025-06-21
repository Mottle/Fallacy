package dev.deepslate.fallacy.race.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.behavior.Behavior
import dev.deepslate.fallacy.behavior.Behaviors
import dev.deepslate.fallacy.common.data.player.NutritionState
import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import dev.deepslate.fallacy.race.Race
import dev.deepslate.fallacy.race.resources.AttributeResource
import dev.deepslate.fallacy.race.resources.NutritionResource
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer

class Zombie : Race() {

    companion object {
        val ID = Fallacy.withID("zombie")
        val TAGS = listOf(
            Behaviors.UNDEAD, Behaviors.WEAKNESS_IN_SUNLIGHT, Behaviors.WEAKNESS_IN_DAY,
            Behaviors.BURNING_IN_SUNLIGHT
        )
    }

    override val namespacedId: ResourceLocation = ID

//    override val attribute: PlayerAttribute =
//        PlayerAttribute(health = 40.0, attackDamage = 4.0, strength = 2.0, armor = 4.0)
//
//    override val nutrition: NutritionState = NutritionState.noNeed()

    override val resources: Map<String, Resource> = mapOf(
        AttributeResource.KEY to AttributeResource(
            PlayerAttribute(health = 40.0, attackDamage = 4.0, strength = 2.0, armor = 4.0)
        ),
        NutritionResource.KEY to NutritionResource(NutritionState.noNeed())
    )

    override fun apply(player: ServerPlayer) {
        Behavior.addAll(player, TAGS)
    }

    override fun deapply(player: ServerPlayer) {
        Behavior.removeAll(player, TAGS)
    }
}