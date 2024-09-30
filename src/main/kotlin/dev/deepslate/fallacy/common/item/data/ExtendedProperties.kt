package dev.deepslate.fallacy.common.item.data

import dev.deepslate.fallacy.util.extendedProperties
import net.minecraft.world.item.ItemStack

data class ExtendedProperties(val rank: Int, val foodProperties: ExtendedFoodProperties?) {

    class Builder() {

        private var rank = 0

        private var foodProperties: ExtendedFoodProperties? = null

        fun withRank(rank: Int): Builder {
            this.rank = rank
            return this
        }

        fun withFoodProperties(foodProperties: ExtendedFoodProperties): Builder {
            this.foodProperties = foodProperties
            return this
        }

        fun build(): ExtendedProperties = ExtendedProperties(rank, foodProperties)
    }

    companion object {
        fun onItemStack(itemStack: ItemStack) {
            val item = itemStack.item
            val extended = item.extendedProperties ?: return

            if (extended.foodProperties != null) {
                ExtendedFoodProperties.onItemStack(itemStack, extended.foodProperties)
            }
        }
    }
}