package dev.deepslate.fallacy.common.capability.diet

import dev.deepslate.fallacy.common.data.player.FoodHistory
import dev.deepslate.fallacy.common.data.player.NutritionState
import dev.deepslate.fallacy.common.item.component.FallacyDataComponents
import dev.deepslate.fallacy.common.item.component.NutritionData
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

interface IDiet {

    val player: Player

    var history: FoodHistory

    var nutrition: NutritionState

    fun sync()

    //food必须保证可食用
    fun eat(food: ItemStack) {
        if (food.has(FallacyDataComponents.NUTRITION)) {
            val nutrition = food.get(FallacyDataComponents.NUTRITION)!!
            handleNutrition(nutrition)
        }

        val fullLevel = food.get(FallacyDataComponents.FULL_LEVEL) ?: 2
        val effect = getFullEffectInstance(fullLevel)
        player.addEffect(effect)

        history = history.addFood(food)
    }

    fun getFullEffectInstance(fullLevel: Int): MobEffectInstance

    fun getEatDurationMultiple(food: ItemStack): Float

    fun handleNutrition(nutrition: NutritionData)
}