package dev.deepslate.fallacy.common.item

import com.tterrag.registrate.util.entry.ItemEntry
import dev.deepslate.fallacy.common.FallacyTabs
import dev.deepslate.fallacy.common.block.FallacyBlocks
import dev.deepslate.fallacy.common.item.component.NutritionData
import dev.deepslate.fallacy.common.item.data.ExtendedFoodProperties
import dev.deepslate.fallacy.common.item.data.ExtendedProperties
import dev.deepslate.fallacy.common.item.items.*
import dev.deepslate.fallacy.common.registrate.REG
import dev.deepslate.fallacy.common.registrate.defaultModelWithTexture
import dev.deepslate.fallacy.common.registrate.formattedLang
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Rarity

object FallacyItems {
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

    val CROP = CropItems

    val RACE = RaceItems

    val GEOLOGY = GeologyItems

    val TOOL = ToolItems

    val MATERIAL = MaterialItems
}