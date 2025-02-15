package dev.deepslate.fallacy.common.item.data

data class ExtendedProperties(val rank: Int, val foodProperties: ExtendedFoodProperties?, val deprecated: Boolean) :
    ExtendedPropertiesLike {

    class Builder() : ExtendedPropertiesLike {

        private var rank = 0

        private var foodProperties: ExtendedFoodProperties? = null

        private var deprecated = false

        fun withRank(rank: Int): Builder {
            this.rank = rank
            return this
        }

        fun withFoodProperties(foodProperties: ExtendedFoodProperties): Builder {
            this.foodProperties = foodProperties
            return this
        }

        fun deprecated(): Builder {
            this.deprecated = true
            return this
        }

        fun build(): ExtendedProperties = ExtendedProperties(rank, foodProperties, deprecated)

        override fun get(): ExtendedProperties = build()
    }

    override fun get(): ExtendedProperties = this

    companion object {

        fun default() = Builder().build()

//        fun onItemStack(itemStack: ItemStack) {
//            val item = itemStack.item
//            val extended = item.internalExtendedProperties ?: return
//
//            if (extended.foodProperties != null) {
//                ExtendedFoodProperties.onItemStack(itemStack, extended.foodProperties)
//            }
//        }
    }
}