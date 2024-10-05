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
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity

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
        }.lang("miu berry").defaultModelWithTexture("nature/miu_berries").tab(FallacyTabs.NATURE.key!!).register()

    val MIU_BERRY_BUSH_SEEDS: ItemEntry<FallacyItemNameBlockItem> = REG.item("miu_berry_bush_seeds") {
        FallacyItemNameBlockItem(
            FallacyBlocks.MIU_BERRY_BUSH,
            it.rarity(Rarity.UNCOMMON),
            ExtendedProperties.default()
        )
    }.lang("miu berry bush seeds").defaultModelWithTexture("nature/miu_berry_bush_seeds")
        .tab(FallacyTabs.NATURE.key!!).register()

    object Crop {
        val BARLEY: ItemEntry<FallacyItem> = REG.item("barley") {
            FallacyItem(
                it, ExtendedProperties.Builder().withFoodProperties(
                    ExtendedFoodProperties.Builder().withFullLevel(2)
                        .withNutrition(NutritionData(carbohydrate = 0.1f, fiber = 0.1f)).build()
                ).build()
            )
        }.properties {
            it.food(FoodProperties.Builder().nutrition(1).saturationModifier(0.5f).build())
        }.lang("barley").defaultModelWithTexture("crop/barley").tab(FallacyTabs.FARMING.key!!).register()

        val BARLEY_SEEDS: ItemEntry<FallacyItemNameBlockItem> = REG.item("barley_seeds") {
            FallacyItemNameBlockItem(FallacyBlocks.Crop.BARLEY, it, ExtendedProperties.default())
        }.lang("barley seeds").defaultModelWithTexture("crop/barley_seeds").tab(FallacyTabs.FARMING.key!!).register()

        val OAT: ItemEntry<FallacyItem> = REG.item("oat") {
            FallacyItem(
                it, ExtendedProperties.Builder().withFoodProperties(
                    ExtendedFoodProperties.Builder().withFullLevel(2)
                        .withNutrition(NutritionData(carbohydrate = 0.1f, fiber = 0.1f)).build()
                ).build()
            )
        }.properties {
            it.food(FoodProperties.Builder().nutrition(1).saturationModifier(0.5f).build())
        }.lang("oat").defaultModelWithTexture("crop/oat").tab(FallacyTabs.FARMING.key!!).register()

        val OAT_SEEDS: ItemEntry<FallacyItemNameBlockItem> = REG.item("oat_seeds") {
            FallacyItemNameBlockItem(FallacyBlocks.Crop.OAT, it, ExtendedProperties.default())
        }.lang("oat seeds").defaultModelWithTexture("crop/oat_seeds").tab(FallacyTabs.FARMING.key!!).register()

        val SOYBEAN: ItemEntry<FallacyItemNameBlockItem> = REG.item("soybean") {
            FallacyItemNameBlockItem(
                FallacyBlocks.Crop.SOYBEAN, it, ExtendedProperties.Builder().withFoodProperties(
                    ExtendedFoodProperties.Builder().withFullLevel(2)
                        .withNutrition(NutritionData(carbohydrate = 0.2f, fat = 0.2f)).build()
                ).build()
            )
        }.properties {
            it.food(FoodProperties.Builder().nutrition(1).saturationModifier(0.5f).build())
        }.lang("soybean").defaultModelWithTexture("crop/soybean").tab(FallacyTabs.FARMING.key!!).register()

        val TOMATO = REG.item("tomato") {
            FallacyItem(
                it, ExtendedProperties.Builder().withFoodProperties(
                    ExtendedFoodProperties.Builder().withFullLevel(2)
                        .withNutrition(NutritionData(carbohydrate = 0.1f, fiber = 0.1f, electrolyte = 0.1f)).build()
                ).build()
            )
        }.properties {
            it.food(FoodProperties.Builder().nutrition(1).saturationModifier(0.5f).build())
        }.lang("tomato").defaultModelWithTexture("crop/tomato").tab(FallacyTabs.FARMING.key!!).register()

        val TOMATO_SEEDS = REG.item("tomato_seeds") {
            FallacyItemNameBlockItem(FallacyBlocks.Crop.TOMATO, it, ExtendedProperties.default())
        }.lang("tomato seeds").defaultModelWithTexture("crop/tomato_seeds").tab(FallacyTabs.FARMING.key!!).register()

        val SPINACH = REG.item("spinach") {
            FallacyItem(
                it,
                ExtendedProperties.Builder().withFoodProperties(
                    ExtendedFoodProperties.Builder().withFullLevel(2)
                        .withNutrition(NutritionData(fiber = 0.3f, electrolyte = 0.2f)).build()
                ).build()
            )
        }.properties {
            it.food(FoodProperties.Builder().nutrition(1).saturationModifier(0.5f).build())
        }.lang("spinach").defaultModelWithTexture("crop/spinach").tab(FallacyTabs.FARMING.key!!).register()

        val SPINACH_SEEDS = REG.item("spinach_seeds") {
            FallacyItemNameBlockItem(FallacyBlocks.Crop.SPINACH, it, ExtendedProperties.default())
        }.lang("spinach seeds").defaultModelWithTexture("crop/spinach_seeds").tab(FallacyTabs.FARMING.key!!).register()
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