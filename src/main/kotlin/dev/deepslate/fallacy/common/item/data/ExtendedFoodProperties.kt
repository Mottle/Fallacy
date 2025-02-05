package dev.deepslate.fallacy.common.item.data

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.item.component.FallacyDataComponents
import dev.deepslate.fallacy.common.item.component.NutritionData
import dev.deepslate.fallacy.util.extension.extendedProperties
import net.minecraft.core.component.DataComponents
import net.minecraft.world.food.FoodProperties
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent

data class ExtendedFoodProperties(val fullLevel: Int, val nutrition: NutritionData, val eatDurationTicks: Int) {
    class Builder {

        //聊胜于无-零嘴-简餐-大餐-饕餮盛宴
        private var fullLevel: Int = 2

        private var nutrition: NutritionData = NutritionData()

        private var eatenDurationTicks: Int = -1

        //8 16 32 48 64
        private fun getEatenTicks(fullLevel: Int): Int = if (fullLevel == 0) 8 else fullLevel * 16

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
            val eatenDuration = if (eatenDurationTicks != -1) eatenDurationTicks else getEatenTicks(fullLevel)

            return ExtendedFoodProperties(fullLevel, nutrition, eatenDuration)
        }
    }

//    companion object {
//
//        fun onItemStack(itemStack: ItemStack, properties: ExtendedFoodProperties) {
//            if (!itemStack.has(DataComponents.FOOD)) return
//
//            val foodData = itemStack.get(DataComponents.FOOD)!!
//            val fixedFoodData = FoodProperties(
//                foodData.nutrition,
//                foodData.saturation,
//                foodData.canAlwaysEat,
//                properties.eatDurationTicks.toFloat() / 20f,
//                foodData.usingConvertsTo,
//                foodData.effects
//            )
//
//            itemStack.set(DataComponents.FOOD, fixedFoodData)
//            itemStack.set(FallacyDataComponents.NUTRITION, properties.nutrition)
//            itemStack.set(FallacyDataComponents.FULL_LEVEL, properties.fullLevel)
//        }
//    }

    @EventBusSubscriber(modid = Fallacy.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    object Handler {
        @SubscribeEvent(priority = EventPriority.LOWEST)
        fun onModifyDefaultComponent(event: ModifyDefaultComponentsEvent) {
            val item = event.allItems.filter { it.components().has(DataComponents.FOOD) }
                .filter { it.extendedProperties?.foodProperties != null }.forEach { item ->
                    val foodData = item.components().get(DataComponents.FOOD)!!
                    val properties = item.extendedProperties!!.foodProperties!!

                    val fixedFoodData = FoodProperties(
                        foodData.nutrition,
                        foodData.saturation,
                        foodData.canAlwaysEat,
                        properties.eatDurationTicks.toFloat() / 20f,
                        foodData.usingConvertsTo,
                        foodData.effects
                    )

                    event.modify(item) { builder ->
                        builder.set(DataComponents.FOOD, fixedFoodData)
                        builder.set(FallacyDataComponents.NUTRITION.get(), properties.nutrition)
                        builder.set(FallacyDataComponents.FULL_LEVEL.get(), properties.fullLevel)
                    }
                }
        }
    }
}