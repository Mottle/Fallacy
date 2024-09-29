package dev.deepslate.fallacy.common.capability.diet

import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.common.effect.FallacyEffects
import dev.deepslate.fallacy.common.item.component.NutritionData
import dev.deepslate.fallacy.util.TickHelper
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

class PlayerDiet(override val player: Player) : IDiet {
    override fun getFullEffectInstance(fullLevel: Int): MobEffectInstance {
        val ticks = TickHelper.second(fullLevel * 20)
        return MobEffectInstance(FallacyEffects.FULL, ticks)
    }

    override fun handleNutrition(nutrition: NutritionData) {
        val nutritionState = player.getData(FallacyAttachments.NUTRITION_STATE)
        val new = nutritionState.add(nutrition)
        new.set(player)
    }

    override fun getEatDurationMultiple(food: ItemStack): Float {
        val count = history.countFood(food)

        return when (count) {
            in 0..2 -> 1f
            in 3..4 -> 1.5f
            in 4..6 -> 3f
            in 7..8 -> 5f
            9 -> 8f
            else -> 12f
        }
    }
}