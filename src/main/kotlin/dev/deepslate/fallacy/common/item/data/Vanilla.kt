package dev.deepslate.fallacy.common.item.data

import dev.deepslate.fallacy.common.item.component.NutritionData
import dev.deepslate.fallacy.util.internalExtendedProperties
import net.minecraft.world.item.Items

object Vanilla {
    private val defaultNutrition = mapOf(
        Items.ENCHANTED_GOLDEN_APPLE to NutritionData(
            carbohydrate = 5f,
            fat = 5f,
            protein = 5f,
            fiber = 5f,
            electrolyte = 5f
        ),
        Items.GOLDEN_APPLE to NutritionData(
            carbohydrate = 1f,
            fat = 1f,
            protein = 1f,
            fiber = 1f,
            electrolyte = 1f
        ),
        Items.GOLDEN_CARROT to NutritionData(carbohydrate = 0.4f, fiber = 0.8f),
        Items.COOKED_BEEF to NutritionData(fat = 0.4f, protein = 1.5f),
        Items.COOKED_PORKCHOP to NutritionData(fat = 1f, protein = 0.8f),
        Items.COOKED_MUTTON to NutritionData(fat = 0.8f, protein = 0.8f),
        Items.COOKED_SALMON to NutritionData(fat = 0.1f, protein = 0.5f),
        Items.BAKED_POTATO to NutritionData(carbohydrate = 1.5f, fat = 0.1f),
        Items.BEETROOT to NutritionData(carbohydrate = 1.5f, fiber = 0.3f),
        Items.BEETROOT_SOUP to NutritionData(carbohydrate = 1.5f),
        Items.BREAD to NutritionData(carbohydrate = 0.8f, fiber = 0.1f),
        Items.CARROT to NutritionData(carbohydrate = 0.4f, fiber = 0.8f),
        Items.COOKED_CHICKEN to NutritionData(fat = 0.7f, protein = 0.8f),
        Items.COOKED_COD to NutritionData(protein = 1f),
        Items.COOKED_RABBIT to NutritionData(protein = 1f),
        Items.RABBIT_STEW to NutritionData(protein = 0.8f),
        Items.APPLE to NutritionData(carbohydrate = 0.6f, fiber = 0.8f, electrolyte = 0.1f),
        Items.CHORUS_FRUIT to NutritionData(electrolyte = 0.6f, fiber = 1f),
        Items.DRIED_KELP to NutritionData(carbohydrate = 0.4f, fiber = 0.4f, electrolyte = 1f),
        Items.MELON_SLICE to NutritionData(carbohydrate = 0.8f, electrolyte = 0.8f),
        Items.POTATO to NutritionData(carbohydrate = 1.2f, fat = 0.1f),
        Items.PUMPKIN_PIE to NutritionData(carbohydrate = 0.6f, fat = 0.3f),
        Items.BEEF to NutritionData(fat = 0.4f, protein = 1f),
        Items.CHICKEN to NutritionData(fat = 0.5f, protein = 0.8f),
        Items.MUTTON to NutritionData(fat = 0.8f, protein = 0.6f),
        Items.PORKCHOP to NutritionData(fat = 0.8f, protein = 0.8f),
        Items.RABBIT to NutritionData(protein = 0.8f),
        Items.SWEET_BERRIES to NutritionData(carbohydrate = 0.2f, electrolyte = 0.2f),
        Items.GLOW_BERRIES to NutritionData(carbohydrate = 0.1f, electrolyte = 0.5f),
        Items.CAKE to NutritionData(carbohydrate = 1f, fat = 0.5f),
        Items.HONEY_BOTTLE to NutritionData(carbohydrate = 2f),
        Items.PUFFERFISH to NutritionData(protein = 2f),
        Items.COD to NutritionData(protein = 1f),
        Items.SALMON to NutritionData(fat = 0.1f, protein = 0.5f),
        Items.TROPICAL_FISH to NutritionData(protein = 1f),
        Items.COOKIE to NutritionData(carbohydrate = 1.2f, fat = 1f),
        Items.MUSHROOM_STEW to NutritionData(carbohydrate = 1f, fat = 0.4f, fiber = 0.5f)
    )

    private val defaultFullLevel = mapOf(
        Items.PUMPKIN_PIE to 1,
        Items.GLOW_BERRIES to 1,
        Items.SWEET_BERRIES to 1,
        Items.COOKIE to 1,
        Items.GOLDEN_APPLE to 3,
        Items.ENCHANTED_GOLDEN_APPLE to 4,
    )

    fun set() {
        (defaultNutrition.keys + defaultFullLevel.keys).map { item ->
            val nutrition = defaultNutrition[item]
            val fullLevel = defaultFullLevel[item]
            val foodData = ExtendedFoodProperties.Builder()

            if (nutrition != null) {
                foodData.withNutrition(nutrition)
            }

            if (fullLevel != null) {
                foodData.withFullLevel(fullLevel)
            }

            item.internalExtendedProperties = ExtendedProperties.Builder().withFoodProperties(foodData.build()).build()
        }
    }
}