package dev.deepslate.fallacy.common.item

import com.tterrag.registrate.util.entry.ItemEntry
import dev.deepslate.fallacy.common.FallacyTabs
import dev.deepslate.fallacy.common.block.FallacyBlocks.MIU_BERRY_BUSH
import dev.deepslate.fallacy.common.item.armor.FallacyArmorMaterials
import dev.deepslate.fallacy.common.registrate.REG
import dev.deepslate.fallacy.common.registrate.defaultModelWithTexture
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemNameBlockItem

object FallacyItems {
    init {
        Race
    }

    val MIU_BERRIES: ItemEntry<Item> = REG.item<Item>("miu_berry", ::Item).properties { p ->
        p.food(FoodProperties.Builder().nutrition(3).saturationModifier(0.5f).fast().build())
    }.lang("miu berry").defaultModelWithTexture("nature/miu_berries").tab(FallacyTabs.NATURE.key!!).register()

    val MIU_BERRY_BUSH_SEED: ItemEntry<Item> = REG.item<Item>("miu_berry_bush_seeds") {
        ItemNameBlockItem(MIU_BERRY_BUSH.get(), it)
    }.lang("miu berry bush seeds").defaultModelWithTexture("nature/miu_berry_bush_seeds")
        .tab(FallacyTabs.NATURE.key!!).register()

    object Race {
        val ROCK_BOOTS: ItemEntry<ArmorItem> = REG.item<ArmorItem>("rock_boots") { _ ->
            ArmorItem(
                FallacyArmorMaterials.Rock,
                ArmorItem.Type.BOOTS, Item.Properties().durability(1000).setNoRepair()
            )
        }.lang("rock skin").defaultModelWithTexture("race/rock_skin").register()

        val ROCK_LEGGINGS: ItemEntry<ArmorItem> = REG.item<ArmorItem>("rock_leggings") { _ ->
            ArmorItem(
                FallacyArmorMaterials.Rock,
                ArmorItem.Type.LEGGINGS, Item.Properties().durability(1000).setNoRepair()
            )
        }.lang("rock skin").defaultModelWithTexture("race/rock_skin").register()

        val ROCK_CHESTPLATE: ItemEntry<ArmorItem> = REG.item<ArmorItem>("rock_chestplate") { _ ->
            ArmorItem(
                FallacyArmorMaterials.Rock,
                ArmorItem.Type.CHESTPLATE, Item.Properties().durability(1000).setNoRepair()
            )
        }.lang("rock skin").defaultModelWithTexture("race/rock_skin").register()

        val ROCK_HELMET: ItemEntry<ArmorItem> = REG.item<ArmorItem>("rock_helmet") { _ ->
            ArmorItem(
                FallacyArmorMaterials.Rock,
                ArmorItem.Type.HELMET, Item.Properties().durability(1000).setNoRepair()
            )
        }.lang("rock skin").defaultModelWithTexture("race/rock_skin").register()
    }
}