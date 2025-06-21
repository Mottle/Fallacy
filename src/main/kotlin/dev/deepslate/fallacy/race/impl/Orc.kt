package dev.deepslate.fallacy.race.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import dev.deepslate.fallacy.race.Race
import dev.deepslate.fallacy.race.resources.AttributeResource
import dev.deepslate.fallacy.race.resources.NutritionResource
import net.minecraft.resources.ResourceLocation

class Orc : Race() {

    companion object {
        val ID = Fallacy.withID("orc")
    }

    override val namespacedId: ResourceLocation = ID

//    override val attribute: PlayerAttribute =
//        PlayerAttribute(health = 40.0, attackDamage = 4.0, armor = 6.0, magicResistance = 10.0, strength = 3.0)
//
//    override val nutrition: NutritionState = NutritionState()

    override val resources: Map<String, Resource> = mapOf(
        AttributeResource.KEY to AttributeResource(
            PlayerAttribute(
                health = 40.0,
                attackDamage = 4.0,
                armor = 6.0,
                magicResistance = 10.0,
                strength = 3.0
            )
        ),
        NutritionResource.KEY to NutritionResource.of()
    )
}