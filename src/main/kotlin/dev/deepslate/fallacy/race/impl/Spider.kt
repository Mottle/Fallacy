package dev.deepslate.fallacy.race.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import dev.deepslate.fallacy.race.Race
import dev.deepslate.fallacy.race.resources.AttributeResource
import dev.deepslate.fallacy.race.resources.NutritionResource
import net.minecraft.resources.ResourceLocation

class Spider : Race() {
    companion object {
        val ID = Fallacy.withID("spider")
    }

    override val namespacedId: ResourceLocation = ID

//    override val attribute: PlayerAttribute =
//        PlayerAttribute(health = 25.0, attackDamage = 3.0, strength = 2.0, armor = 4.0)
//
//    override val nutrition: NutritionState = NutritionState()

    override val resources: Map<String, Resource> = mapOf(
        AttributeResource.KEY to AttributeResource(
            PlayerAttribute(
                health = 25.0,
                attackDamage = 3.0,
                strength = 2.0,
                armor = 4.0
            )
        ),
        NutritionResource.KEY to NutritionResource.of()
    )
}