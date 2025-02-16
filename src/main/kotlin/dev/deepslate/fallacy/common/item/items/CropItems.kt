package dev.deepslate.fallacy.common.item.items

import com.tterrag.registrate.util.entry.ItemEntry
import dev.deepslate.fallacy.common.FallacyTabs
import dev.deepslate.fallacy.common.block.FallacyBlocks
import dev.deepslate.fallacy.common.item.FallacyItem
import dev.deepslate.fallacy.common.item.FallacyItemNameBlockItem
import dev.deepslate.fallacy.common.item.component.NutritionData
import dev.deepslate.fallacy.common.item.data.ExtendedFoodProperties
import dev.deepslate.fallacy.common.item.data.ExtendedProperties
import dev.deepslate.fallacy.common.registrate.REG
import dev.deepslate.fallacy.common.registrate.defaultModelWithTexture
import dev.deepslate.fallacy.common.registrate.defaultModelWithVanillaTexture
import dev.deepslate.fallacy.common.registrate.formattedLang
import net.minecraft.core.Holder
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.food.Foods
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

object CropItems {
    private val seedTags = arrayOf(ItemTags.CHICKEN_FOOD, ItemTags.PARROT_FOOD, ItemTags.VILLAGER_PLANTABLE_SEEDS)

    val WHEAT_SEEDS: ItemEntry<FallacyItemNameBlockItem> = REG.item("wheat_seeds") {
        FallacyItemNameBlockItem(FallacyBlocks.CROP.WHEAT, it, ExtendedProperties.default())
    }.formattedLang().defaultModelWithVanillaTexture("wheat_seeds").tag(*seedTags).tab(FallacyTabs.FARMING.key!!)
        .register()

    val POTATO: ItemEntry<FallacyItemNameBlockItem> = REG.item("potato") {
        FallacyItemNameBlockItem(FallacyBlocks.CROP.POTATOES, it, ExtendedProperties.default())
    }.properties { it.food(Foods.POTATO) }.formattedLang().defaultModelWithVanillaTexture("potato")
        .tag(ItemTags.PIG_FOOD, ItemTags.VILLAGER_PLANTABLE_SEEDS).tab(FallacyTabs.FARMING.key!!).register()

    val CARROT: ItemEntry<FallacyItemNameBlockItem> = REG.item("carrot") {
        FallacyItemNameBlockItem(FallacyBlocks.CROP.CARROTS, it, ExtendedProperties.default())
    }.properties { it.food(Foods.CARROT) }.formattedLang().defaultModelWithVanillaTexture("carrot")
        .tag(ItemTags.PIG_FOOD, ItemTags.VILLAGER_PLANTABLE_SEEDS).tab(FallacyTabs.FARMING.key!!).register()

    val BEETROOT_SEEDS: ItemEntry<FallacyItemNameBlockItem> = REG.item("beetroot_seeds") {
        FallacyItemNameBlockItem(FallacyBlocks.CROP.BEETROOTS, it, ExtendedProperties.default())
    }.formattedLang().defaultModelWithVanillaTexture("beetroot_seeds").tag(*seedTags).tab(FallacyTabs.FARMING.key!!)
        .register()

    val BARLEY: ItemEntry<FallacyItem> =
        crop("barley", 2, NutritionData(carbohydrate = 0.1f, fiber = 0.1f), 1, 0.5f)

    val BARLEY_SEEDS: ItemEntry<FallacyItemNameBlockItem> =
        seeds("barley_seeds") { FallacyBlocks.CROP.BARLEY }

    val OAT: ItemEntry<FallacyItem> = crop("oat", 2, NutritionData(carbohydrate = 0.1f, fiber = 0.1f), 1, 0.5f)

    val OAT_SEEDS: ItemEntry<FallacyItemNameBlockItem> = seeds("oat_seeds") { FallacyBlocks.CROP.OAT }

    val SOYBEAN: ItemEntry<FallacyItemNameBlockItem> =
        seedCrop("soybean", 2, NutritionData(carbohydrate = 0.2f, fat = 0.2f), 1, 0.5f)

    val TOMATO: ItemEntry<FallacyItem> =
        crop("tomato", 2, NutritionData(carbohydrate = 0.1f, fiber = 0.1f, electrolyte = 0.1f), 1, 0.5f)

    val TOMATO_SEEDS: ItemEntry<FallacyItemNameBlockItem> = seeds("tomato_seeds") { FallacyBlocks.CROP.TOMATO }

    val SPINACH: ItemEntry<FallacyItem> =
        crop("spinach", 2, NutritionData(fiber = 0.3f, electrolyte = 0.2f), 1, 0.5f)

    val SPINACH_SEEDS: ItemEntry<FallacyItemNameBlockItem> = seeds("spinach_seeds") { FallacyBlocks.CROP.SPINACH }

    val CHILE_PEPPER: ItemEntry<FallacyItem> =
        crop("chile_pepper", 2, NutritionData(fiber = 0.3f, electrolyte = 0.2f), 1, 0.5f)

    val CHILE_PEPPER_SEEDS: ItemEntry<FallacyItemNameBlockItem> =
        seeds("chile_pepper_seeds") { FallacyBlocks.CROP.CHILE_PEPPER }

    val CORN: ItemEntry<FallacyItem> = crop("corn", 2, NutritionData(carbohydrate = 0.1f, fiber = 0.1f), 1, 0.5f)

    val CORN_SEEDS: ItemEntry<FallacyItemNameBlockItem> = seeds("corn_seeds") { FallacyBlocks.CROP.CORN }

    val EGGPLANT: ItemEntry<FallacyItem> =
        crop("eggplant", 2, NutritionData(carbohydrate = 0.1f, fiber = 0.1f), 1, 0.5f)

    val EGGPLANT_SEEDS: ItemEntry<FallacyItemNameBlockItem> =
        seeds("eggplant_seeds") { FallacyBlocks.CROP.EGGPLANT }

    val ASPARAGUS: ItemEntry<FallacyItem> =
        crop("asparagus", 2, NutritionData(carbohydrate = 0.1f, fiber = 0.1f), 1, 0.5f)

    val ASPARAGUS_SEEDS: ItemEntry<FallacyItemNameBlockItem> =
        seeds("asparagus_seeds") { FallacyBlocks.CROP.ASPARAGUS }

    val CELERY: ItemEntry<FallacyItem> =
        crop("celery", 2, NutritionData(carbohydrate = 0.1f, fiber = 0.1f), 1, 0.5f)

    val CELERY_SEEDS: ItemEntry<FallacyItemNameBlockItem> = seeds("celery_seeds") { FallacyBlocks.CROP.CELERY }

    val CABBAGE: ItemEntry<FallacyItem> =
        crop("cabbage", 2, NutritionData(carbohydrate = 0.1f, fiber = 0.1f), 1, 0.5f)

    val CABBAGE_SEEDS: ItemEntry<FallacyItemNameBlockItem> = seeds("cabbage_seeds") { FallacyBlocks.CROP.CABBAGE }

    private fun seeds(name: String, cropGetter: () -> Holder<Block>): ItemEntry<FallacyItemNameBlockItem> =
        REG.item(name) {
            FallacyItemNameBlockItem(cropGetter(), it, ExtendedProperties.default())
        }.formattedLang().defaultModelWithTexture("crop/$name").tag(*seedTags)
            .tab(FallacyTabs.FARMING.key!!).register()

    private fun crop(
        name: String,
        foodLevel: Int = 2,
        nutritionData: NutritionData = NutritionData(),
        nutrition: Int = 1,
        saturation: Float = 0.5f,
        tags: Array<out TagKey<Item>> = arrayOf()
    ): ItemEntry<FallacyItem> = REG.item(name) {
        FallacyItem(
            it, ExtendedProperties.Builder().withFoodProperties(
                ExtendedFoodProperties.Builder().withFullLevel(foodLevel)
                    .withNutrition(nutritionData).build()
            ).build()
        )
    }.properties {
        it.food(FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturation).build())
    }.formattedLang().defaultModelWithTexture("crop/$name").tab(FallacyTabs.FARMING.key!!).tag(*tags).register()

    fun seedCrop(
        name: String,
        foodLevel: Int = 2,
        nutritionData: NutritionData = NutritionData(),
        nutrition: Int = 1,
        saturation: Float = 0.5f,
        tags: Array<out TagKey<Item>> = arrayOf()
    ): ItemEntry<FallacyItemNameBlockItem> = REG.item(name) {
        FallacyItemNameBlockItem(
            FallacyBlocks.CROP.SOYBEAN, it, ExtendedProperties.Builder().withFoodProperties(
                ExtendedFoodProperties.Builder().withFullLevel(foodLevel)
                    .withNutrition(nutritionData).build()
            ).build()
        )
    }.properties {
        it.food(FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturation).build())
    }.formattedLang().defaultModelWithTexture("crop/$name").tab(FallacyTabs.FARMING.key!!).tag(*tags).register()
}