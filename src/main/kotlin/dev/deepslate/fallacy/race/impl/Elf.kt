package dev.deepslate.fallacy.race.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import dev.deepslate.fallacy.race.Race
import dev.deepslate.fallacy.race.resources.AttributeResource
import dev.deepslate.fallacy.race.resources.NutritionResource
import net.minecraft.resources.ResourceLocation

class Elf : Race() {

    companion object {
        val ID = Fallacy.withID("elf")
    }

    override val namespacedId: ResourceLocation = ID

    override val resources: Map<String, Resource> = mapOf(
        AttributeResource.KEY to AttributeResource(
            PlayerAttribute(
                moveSpeed = 0.12,
                magicResistance = 25.0,
                strength = 1.0
            )
        ),
        NutritionResource.KEY to NutritionResource.of()
    )
//    override val attribute: PlayerAttribute = PlayerAttribute(moveSpeed = 0.12, magicResistance = 25.0, strength = 1.0)
//
//    override val nutrition: NutritionState = NutritionState()
}