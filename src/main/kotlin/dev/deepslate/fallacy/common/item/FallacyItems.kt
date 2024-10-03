package dev.deepslate.fallacy.common.item

import com.tterrag.registrate.util.entry.ItemEntry
import dev.deepslate.fallacy.common.FallacyTabs
import dev.deepslate.fallacy.common.block.FallacyBlocks.MIU_BERRY_BUSH
import dev.deepslate.fallacy.common.item.armor.FallacyArmorMaterials
import dev.deepslate.fallacy.common.item.component.NutritionData
import dev.deepslate.fallacy.common.item.data.ExtendedFoodProperties
import dev.deepslate.fallacy.common.item.data.ExtendedProperties
import dev.deepslate.fallacy.common.item.data.FallacyItemNameBlockItem
import dev.deepslate.fallacy.common.registrate.REG
import dev.deepslate.fallacy.common.registrate.defaultModelWithTexture
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity

object FallacyItems {
    init {
        Race
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

    val MIU_BERRY_BUSH_SEED: ItemEntry<FallacyItemNameBlockItem> = REG.item("miu_berry_bush_seeds") {
        FallacyItemNameBlockItem(MIU_BERRY_BUSH.get(), it.rarity(Rarity.UNCOMMON), ExtendedProperties.default())
    }.lang("miu berry bush seeds").defaultModelWithTexture("nature/miu_berry_bush_seeds")
        .tab(FallacyTabs.NATURE.key!!).register()

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