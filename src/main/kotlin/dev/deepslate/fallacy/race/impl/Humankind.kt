package dev.deepslate.fallacy.race.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.player.NutritionState
import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import dev.deepslate.fallacy.race.Race
import net.minecraft.resources.ResourceLocation

class Humankind : Race() {

    companion object {
        val ID = Fallacy.withID("humankind")
    }

    override val namespacedId: ResourceLocation = ID

    override val attribute: PlayerAttribute = PlayerAttribute(strength = 1.25)

    override val nutrition: NutritionState = NutritionState()
}