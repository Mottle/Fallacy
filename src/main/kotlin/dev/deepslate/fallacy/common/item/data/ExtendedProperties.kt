package dev.deepslate.fallacy.common.item.data

import dev.deepslate.fallacy.util.extendedProperties
import net.minecraft.world.item.ItemStack

data class ExtendedProperties(val foodProperties: ExtendedFoodProperties?) {

    class Builder() {
        private var foodProperties: ExtendedFoodProperties? = null

        fun withFoodProperties(foodProperties: ExtendedFoodProperties): Builder {
            this.foodProperties = foodProperties
            return this
        }

        fun build(): ExtendedProperties = ExtendedProperties(foodProperties)
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