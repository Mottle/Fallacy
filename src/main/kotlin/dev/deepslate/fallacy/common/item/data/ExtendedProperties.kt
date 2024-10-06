package dev.deepslate.fallacy.common.item.data

import dev.deepslate.fallacy.util.internalExtendedProperties
import net.minecraft.world.item.ItemStack

data class ExtendedProperties(val rank: Int, val foodProperties: ExtendedFoodProperties?, val isDeprecated: Boolean) {

    class Builder() {

        private var rank = 0

        private var foodProperties: ExtendedFoodProperties? = null

        private var isDeprecated = false

        fun withRank(rank: Int): Builder {
            this.rank = rank
            return this
        }

        fun withFoodProperties(foodProperties: ExtendedFoodProperties): Builder {
            this.foodProperties = foodProperties
            return this
        }

        fun deprecated(): Builder {
            this.isDeprecated = true
            return this
        }

        fun build(): ExtendedProperties = ExtendedProperties(rank, foodProperties, isDeprecated)
    }

    companion object {

        fun default() = Builder().build()

        fun onItemStack(itemStack: ItemStack) {
            val item = itemStack.item
            val extended = item.internalExtendedProperties ?: return

            if (extended.foodProperties != null) {
                ExtendedFoodProperties.onItemStack(itemStack, extended.foodProperties)
            }
        }
    }
}