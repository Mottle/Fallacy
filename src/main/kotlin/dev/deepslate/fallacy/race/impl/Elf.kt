package dev.deepslate.fallacy.race.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.player.NutritionState
import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import dev.deepslate.fallacy.race.Race
import net.minecraft.resources.ResourceLocation

class Elf : Race() {

    companion object {
        val ID = Fallacy.withID("elf")
    }

    override val namespacedId: ResourceLocation = ID

    override val attribute: PlayerAttribute = PlayerAttribute(moveSpeed = 0.12, magicResistance = 25.0, strength = 1.0)

    override val nutrition: NutritionState = NutritionState()
}