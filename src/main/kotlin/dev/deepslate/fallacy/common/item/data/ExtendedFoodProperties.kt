package dev.deepslate.fallacy.common.item.data

import dev.deepslate.fallacy.common.item.component.NutritionData

data class ExtendedFoodProperties(val defaultDiet: NutritionData, val eatenDurationTicks: Int) {
    class Builder {
        private var defaultDiet: NutritionData = NutritionData()

        private var eatenDurationTicks: Int = 16

        fun withDefaultDiet(defaultDiet: NutritionData): Builder {
            this.defaultDiet = defaultDiet
            return this
        }

        fun withEatenDurationTicks(eatenDurationTicks: Int): Builder {
            this.eatenDurationTicks = eatenDurationTicks
            return this
        }

        fun build(): ExtendedFoodProperties = ExtendedFoodProperties(defaultDiet, eatenDurationTicks)
    }
}