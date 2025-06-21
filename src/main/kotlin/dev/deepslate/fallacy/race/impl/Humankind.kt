package dev.deepslate.fallacy.race.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import dev.deepslate.fallacy.race.Race
import dev.deepslate.fallacy.race.resources.AttributeResource
import dev.deepslate.fallacy.race.resources.NutritionResource
import net.minecraft.resources.ResourceLocation

class Humankind : Race() {

    companion object {
        val ID = Fallacy.withID("humankind")
    }

    override val namespacedId: ResourceLocation = ID

//    override val attribute: PlayerAttribute = PlayerAttribute(strength = 1.25)
//
//    override val nutrition: NutritionState = NutritionState()

    override val resources: Map<String, Resource> = mapOf(
        AttributeResource.KEY to AttributeResource(PlayerAttribute(strength = 1.25)),
        NutritionResource.KEY to NutritionResource.of()
    )
}