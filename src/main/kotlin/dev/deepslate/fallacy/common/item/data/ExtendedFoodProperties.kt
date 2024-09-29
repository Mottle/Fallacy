package dev.deepslate.fallacy.common.item.data

import dev.deepslate.fallacy.common.item.component.DietData

data class ExtendedFoodProperties(val defaultDiet: DietData, val eatenDurationTicks: Int) {
    class Builder {
        private var defaultDiet: DietData = DietData()

        private var eatenDurationTicks: Int = 16

        fun withDefaultDiet(defaultDiet: DietData): Builder {
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