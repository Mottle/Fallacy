package dev.deepslate.fallacy.common.item

import com.tterrag.registrate.util.entry.ItemEntry
import dev.deepslate.fallacy.common.FallacyTabs
import dev.deepslate.fallacy.common.block.FallacyBlocks.MIU_BERRY_BUSH
import dev.deepslate.fallacy.common.registrate.REG
import dev.deepslate.fallacy.common.registrate.defaultModelWithTexture
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemNameBlockItem

object FallacyItems {
    val MIU_BERRIES: ItemEntry<Item> = REG.item<Item>("miu_berry", ::Item).properties { p ->
        p.food(FoodProperties.Builder().nutrition(3).saturationModifier(0.5f).fast().build())
    }.lang("miu berry").defaultModelWithTexture("nature/miu_berries").tab(FallacyTabs.NATURE.key!!).register()

    val MIU_BERRY_BUSH_SEED: ItemEntry<Item> = REG.item<Item>("miu_berry_bush_seeds") {
        ItemNameBlockItem(MIU_BERRY_BUSH.get(), it)
    }.lang("miu berry bush seeds").defaultModelWithTexture("nature/miu_berry_bush_seeds")
        .tab(FallacyTabs.NATURE.key!!).register()
}