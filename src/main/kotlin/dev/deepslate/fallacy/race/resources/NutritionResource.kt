package dev.deepslate.fallacy.race.resources

import dev.deepslate.fallacy.common.capability.FallacyCapabilities
import dev.deepslate.fallacy.common.capability.Synchronous
import dev.deepslate.fallacy.common.data.player.NutritionState
import dev.deepslate.fallacy.race.Race
import net.minecraft.world.entity.player.Player

class NutritionResource(val nutrition: NutritionState) : Race.Resource {
    companion object {
        const val KEY = "nutrition"

        fun of() = NutritionResource(NutritionState())
    }

    override fun apply(player: Player) {
        val diet = player.getCapability(FallacyCapabilities.DIET)!!
        diet.nutrition = nutrition

        if (diet is Synchronous) {
            diet.synchronize()
        }
    }

    override fun deapply(player: Player) {}
}