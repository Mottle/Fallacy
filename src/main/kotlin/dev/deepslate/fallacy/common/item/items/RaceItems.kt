package dev.deepslate.fallacy.common.item.items

import com.tterrag.registrate.util.entry.ItemEntry
import dev.deepslate.fallacy.common.item.armor.FallacyArmorMaterials
import dev.deepslate.fallacy.common.registrate.REG
import dev.deepslate.fallacy.common.registrate.defaultModelWithTexture
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.Item

object RaceItems {
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