package dev.deepslate.fallacy.common.item.data

import dev.deepslate.fallacy.common.item.component.FallacyDataComponents
import dev.deepslate.fallacy.common.item.component.NutritionData
import net.minecraft.core.component.DataComponents
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ItemStack

data class ExtendedFoodProperties(val fullLevel: Int, val nutrition: NutritionData, val eatDurationTicks: Int) {
    class Builder {

        //聊胜于无-零嘴-简餐-大餐-饕餮盛宴
        private var fullLevel: Int = 2

        private var nutrition: NutritionData = NutritionData()

        private var eatenDurationTicks: Int = -1

        private fun calEatenTicks(fullLevel: Int): Int = if (fullLevel == 0) 4 else fullLevel * 8

        fun withFullLevel(fullLevel: Int): Builder {
            this.fullLevel = fullLevel
            return this
        }

        fun withNutrition(defaultDiet: NutritionData): Builder {
            this.nutrition = defaultDiet
            return this
        }

        fun withEatenDurationTicks(eatenDurationTicks: Int): Builder {
            this.eatenDurationTicks = eatenDurationTicks
            return this
        }

        fun build(): ExtendedFoodProperties {
            val eatenDuration = if (eatenDurationTicks != -1) eatenDurationTicks else calEatenTicks(fullLevel)

            return ExtendedFoodProperties(fullLevel, nutrition, eatenDuration)
        }
    }

    companion object {
        fun onItemStack(itemStack: ItemStack, properties: ExtendedFoodProperties) {
            if (!itemStack.has(DataComponents.FOOD)) return

            val foodData = itemStack.get(DataComponents.FOOD)!!
            val fixedFoodData = FoodProperties(
                foodData.nutrition,
                foodData.saturation,
                foodData.canAlwaysEat,
                properties.eatDurationTicks.toFloat() / 20f,
                foodData.usingConvertsTo,
                foodData.effects
            )

            itemStack.set(DataComponents.FOOD, fixedFoodData)
            itemStack.set(FallacyDataComponents.FULL_LEVEL, properties.fullLevel)
        }
    }
}