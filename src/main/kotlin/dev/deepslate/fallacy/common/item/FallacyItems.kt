package dev.deepslate.fallacy.common.item

import com.tterrag.registrate.util.entry.ItemEntry
import dev.deepslate.fallacy.common.FallacyTabs
import dev.deepslate.fallacy.common.block.FallacyBlocks
import dev.deepslate.fallacy.common.item.armor.FallacyArmorMaterials
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
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.block.Block

object FallacyItems {
    init {
        Race
        Crop
    }

    val MIU_BERRIES: ItemEntry<FallacyItem> =
        REG.item("miu_berry") {
            FallacyItem(
                it, ExtendedProperties.Builder().withFoodProperties(
                    ExtendedFoodProperties.Builder().withFullLevel(1)
                        .withNutrition(NutritionData(carbohydrate = 0.3f, fiber = 0.5f)).build()
                ).build()
            )
        }.properties {
            it.food(FoodProperties.Builder().nutrition(3).saturationModifier(0.5f).fast().build())
        }.formattedLang().defaultModelWithTexture("nature/miu_berries").tab(FallacyTabs.NATURE.key!!).register()

    val MIU_BERRY_BUSH_SEEDS: ItemEntry<FallacyItemNameBlockItem> = REG.item("miu_berry_bush_seeds") {
        FallacyItemNameBlockItem(
            FallacyBlocks.MIU_BERRY_BUSH,
            it.rarity(Rarity.UNCOMMON),
            ExtendedProperties.default()
        )
    }.formattedLang().defaultModelWithTexture("nature/miu_berry_bush_seeds")
        .tab(FallacyTabs.NATURE.key!!).register()

    object Crop {
        private val seedTags = arrayOf(ItemTags.CHICKEN_FOOD, ItemTags.PARROT_FOOD, ItemTags.VILLAGER_PLANTABLE_SEEDS)

        val WHEAT_SEEDS: ItemEntry<FallacyItemNameBlockItem> = REG.item("wheat_seeds") {
            FallacyItemNameBlockItem(FallacyBlocks.Crop.WHEAT, it, ExtendedProperties.default())
        }.formattedLang().defaultModelWithVanillaTexture("wheat_seeds").tag(*seedTags).tab(FallacyTabs.FARMING.key!!)
            .register()

        val POTATO: ItemEntry<FallacyItemNameBlockItem> = REG.item("potato") {
            FallacyItemNameBlockItem(FallacyBlocks.Crop.POTATOES, it, ExtendedProperties.default())
        }.properties { it.food(Foods.POTATO) }.formattedLang().defaultModelWithVanillaTexture("potato")
            .tag(ItemTags.PIG_FOOD, ItemTags.VILLAGER_PLANTABLE_SEEDS).tab(FallacyTabs.FARMING.key!!).register()

        val CARROT: ItemEntry<FallacyItemNameBlockItem> = REG.item("carrot") {
            FallacyItemNameBlockItem(FallacyBlocks.Crop.CARROTS, it, ExtendedProperties.default())
        }.properties { it.food(Foods.CARROT) }.formattedLang().defaultModelWithVanillaTexture("carrot")
            .tag(ItemTags.PIG_FOOD, ItemTags.VILLAGER_PLANTABLE_SEEDS).tab(FallacyTabs.FARMING.key!!).register()

        val BEETROOT_SEEDS: ItemEntry<FallacyItemNameBlockItem> = REG.item("beetroot_seeds") {
            FallacyItemNameBlockItem(FallacyBlocks.Crop.BEETROOTS, it, ExtendedProperties.default())
        }.formattedLang().defaultModelWithVanillaTexture("beetroot_seeds").tag(*seedTags).tab(FallacyTabs.FARMING.key!!)
            .register()

        val BARLEY: ItemEntry<FallacyItem> =
            crop("barley", 2, NutritionData(carbohydrate = 0.1f, fiber = 0.1f), 1, 0.5f)

        val BARLEY_SEEDS: ItemEntry<FallacyItemNameBlockItem> =
            seeds("barley_seeds") { FallacyBlocks.Crop.BARLEY }

        val OAT: ItemEntry<FallacyItem> = crop("oat", 2, NutritionData(carbohydrate = 0.1f, fiber = 0.1f), 1, 0.5f)

        val OAT_SEEDS: ItemEntry<FallacyItemNameBlockItem> = seeds("oat_seeds") { FallacyBlocks.Crop.OAT }

        val SOYBEAN: ItemEntry<FallacyItemNameBlockItem> =
            seedCrop("soybean", 2, NutritionData(carbohydrate = 0.2f, fat = 0.2f), 1, 0.5f)

        val TOMATO: ItemEntry<FallacyItem> =
            crop("tomato", 2, NutritionData(carbohydrate = 0.1f, fiber = 0.1f, electrolyte = 0.1f), 1, 0.5f)

        val TOMATO_SEEDS: ItemEntry<FallacyItemNameBlockItem> = seeds("tomato_seeds") { FallacyBlocks.Crop.TOMATO }

        val SPINACH: ItemEntry<FallacyItem> =
            crop("spinach", 2, NutritionData(fiber = 0.3f, electrolyte = 0.2f), 1, 0.5f)

        val SPINACH_SEEDS: ItemEntry<FallacyItemNameBlockItem> = seeds("spinach_seeds") { FallacyBlocks.Crop.SPINACH }

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
        ) = REG.item(name) {
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
        ) = REG.item(name) {
            FallacyItemNameBlockItem(
                FallacyBlocks.Crop.SOYBEAN, it, ExtendedProperties.Builder().withFoodProperties(
                    ExtendedFoodProperties.Builder().withFullLevel(foodLevel)
                        .withNutrition(nutritionData).build()
                ).build()
            )
        }.properties {
            it.food(FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturation).build())
        }.formattedLang().defaultModelWithTexture("crop/$name").tab(FallacyTabs.FARMING.key!!).tag(*tags).register()
    }

    object Race {
//        val GYNOU_WINGS = REG.item<ElytraItem>("gynou_wings") {
//            ElytraItem(Item.Properties().component(DataComponents.UNBREAKABLE, Unbreakable(true)))
//        }.lang("gynou wings").register()

        val ROCK_SKIN_BOOTS: ItemEntry<ArmorItem> = REG.item<ArmorItem>("rock_skin_boots") { _ ->
            ArmorItem(
                FallacyArmorMaterials.Rock,
                ArmorItem.Type.BOOTS, Item.Properties().durability(1000).setNoRepair()
            )
        }.lang("rock skin").defaultModelWithTexture("race/rock_skin").register()

        val ROCK_SKIN_LEGGINGS: ItemEntry<ArmorItem> = REG.item<ArmorItem>("rock_skin_leggings") { _ ->
            ArmorItem(
                FallacyArmorMaterials.Rock,
                ArmorItem.Type.LEGGINGS, Item.Properties().durability(1000).setNoRepair()
            )
        }.lang("rock skin").defaultModelWithTexture("race/rock_skin").register()

        val ROCK_SKIN_CHESTPLATE: ItemEntry<ArmorItem> = REG.item<ArmorItem>("rock_skin_chestplate") { _ ->
            ArmorItem(
                FallacyArmorMaterials.Rock,
                ArmorItem.Type.CHESTPLATE, Item.Properties().durability(1000).setNoRepair()
            )
        }.lang("rock skin").defaultModelWithTexture("race/rock_skin").register()

        val ROCK_SKIN_HELMET: ItemEntry<ArmorItem> = REG.item<ArmorItem>("rock_skin_helmet") { _ ->
            ArmorItem(
                FallacyArmorMaterials.Rock,
                ArmorItem.Type.HELMET, Item.Properties().durability(1000).setNoRepair()
            )
        }.lang("rock skin").defaultModelWithTexture("race/rock_skin").register()

        //BROKEN
        val ROCK_SKIN_BROKEN_BOOTS: ItemEntry<ArmorItem> = REG.item<ArmorItem>("rock_skin_broken_boots") { _ ->
            ArmorItem(
                FallacyArmorMaterials.BROKEN_ROCK,
                ArmorItem.Type.BOOTS, Item.Properties().durability(1000).setNoRepair()
            )
        }.lang("rock skin").defaultModelWithTexture("race/rock_skin").register()

        val ROCK_SKIN_BROKEN_LEGGINGS: ItemEntry<ArmorItem> = REG.item<ArmorItem>("rock_skin_broken_leggings") { _ ->
            ArmorItem(
                FallacyArmorMaterials.BROKEN_ROCK,
                ArmorItem.Type.LEGGINGS, Item.Properties().durability(1000).setNoRepair()
            )
        }.lang("rock skin").defaultModelWithTexture("race/rock_skin").register()

        val ROCK_SKIN_BROKEN_CHESTPLATE: ItemEntry<ArmorItem> =
            REG.item<ArmorItem>("rock_skin_broken_chestplate") { _ ->
                ArmorItem(
                    FallacyArmorMaterials.BROKEN_ROCK,
                    ArmorItem.Type.CHESTPLATE, Item.Properties().durability(1000).setNoRepair()
                )
            }.lang("rock skin").defaultModelWithTexture("race/rock_skin").register()

        val ROCK_SKIN_BROKEN_HELMET: ItemEntry<ArmorItem> = REG.item<ArmorItem>("rock_skin_broken_helmet") { _ ->
            ArmorItem(
                FallacyArmorMaterials.BROKEN_ROCK,
                ArmorItem.Type.HELMET, Item.Properties().durability(1000).setNoRepair()
            )
        }.lang("rock skin").defaultModelWithTexture("race/rock_skin").register()
    }
}