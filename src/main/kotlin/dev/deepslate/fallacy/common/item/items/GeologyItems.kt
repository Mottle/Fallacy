package dev.deepslate.fallacy.common.item.items

import com.tterrag.registrate.providers.DataGenContext
import com.tterrag.registrate.providers.RegistrateItemModelProvider
import com.tterrag.registrate.util.entry.ItemEntry
import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.FallacyTabs
import dev.deepslate.fallacy.common.block.FallacyBlocks
import dev.deepslate.fallacy.common.item.FallacyItemNameBlockItem
import dev.deepslate.fallacy.common.item.FallacyItemTags
import dev.deepslate.fallacy.common.item.data.ExtendedProperties
import dev.deepslate.fallacy.common.registrate.REG
import dev.deepslate.fallacy.common.registrate.formattedLang
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item

object GeologyItems {
    val GNEISS: ItemEntry<FallacyItemNameBlockItem> = REG.item("gneiss") {
        FallacyItemNameBlockItem(FallacyBlocks.GEOLOGY.GNEISS, it, ExtendedProperties.default())
    }.formattedLang().model(::withModel).tag(FallacyItemTags.METAMORPHIC_ROCK).tab(tabKey).register()

    val MARBLE: ItemEntry<FallacyItemNameBlockItem> = REG.item("marble") {
        FallacyItemNameBlockItem(FallacyBlocks.GEOLOGY.MARBLE, it, ExtendedProperties.default())
    }.formattedLang().model(::withModel).tag(FallacyItemTags.METAMORPHIC_ROCK).tab(tabKey).register()

    val SCHIST: ItemEntry<FallacyItemNameBlockItem> = REG.item("schist") {
        FallacyItemNameBlockItem(FallacyBlocks.GEOLOGY.SCHIST, it, ExtendedProperties.default())
    }.formattedLang().model(::withModel).tag(FallacyItemTags.METAMORPHIC_ROCK).tab(tabKey).register()

    val QUARTZITE: ItemEntry<FallacyItemNameBlockItem> = REG.item("quartzite") {
        FallacyItemNameBlockItem(FallacyBlocks.GEOLOGY.QUARTZITE, it, ExtendedProperties.default())
    }.formattedLang().model(::withModel).tag(FallacyItemTags.METAMORPHIC_ROCK).tab(tabKey).register()

    val SKARN: ItemEntry<FallacyItemNameBlockItem> = REG.item("skarn") {
        FallacyItemNameBlockItem(FallacyBlocks.GEOLOGY.SKARN, it, ExtendedProperties.default())
    }.formattedLang().model(::withModel).tag(FallacyItemTags.METAMORPHIC_ROCK).tab(tabKey).register()

    val PHYLLITE: ItemEntry<FallacyItemNameBlockItem> = REG.item("phyllite") {
        FallacyItemNameBlockItem(FallacyBlocks.GEOLOGY.PHYLLITE, it, ExtendedProperties.default())
    }.formattedLang().model(::withModel).tag(FallacyItemTags.METAMORPHIC_ROCK).tab(tabKey).register()

    val SHALE: ItemEntry<FallacyItemNameBlockItem> = REG.item("shale") {
        FallacyItemNameBlockItem(FallacyBlocks.GEOLOGY.SHALE, it, ExtendedProperties.default())
    }.formattedLang().model(::withModel).tag(FallacyItemTags.SEDIMENTARY_ROCK).tab(tabKey).register()

    val CONGLOMERATE: ItemEntry<FallacyItemNameBlockItem> = REG.item("conglomerate") {
        FallacyItemNameBlockItem(FallacyBlocks.GEOLOGY.CONGLOMERATE, it, ExtendedProperties.default())
    }.formattedLang().model(::withModel).tag(FallacyItemTags.SEDIMENTARY_ROCK).tab(tabKey).register()

    val PEAT: ItemEntry<FallacyItemNameBlockItem> = REG.item("peat") {
        FallacyItemNameBlockItem(FallacyBlocks.GEOLOGY.PEAT, it, ExtendedProperties.default())
    }.formattedLang().model(::withModel).tag(FallacyItemTags.SEDIMENTARY_ROCK).tab(tabKey).register()

    val LIMESTONE: ItemEntry<FallacyItemNameBlockItem> = REG.item("limestone") {
        FallacyItemNameBlockItem(FallacyBlocks.GEOLOGY.LIMESTONE, it, ExtendedProperties.default())
    }.formattedLang().model(::withModel).tag(FallacyItemTags.SEDIMENTARY_ROCK).tab(tabKey).register()

    val HALITE: ItemEntry<FallacyItemNameBlockItem> = REG.item("halite") {
        FallacyItemNameBlockItem(FallacyBlocks.GEOLOGY.HALITE, it, ExtendedProperties.default())
    }.formattedLang().model(::withModel).tag(FallacyItemTags.SEDIMENTARY_ROCK).tab(tabKey).register()

    val FOSSIL: ItemEntry<FallacyItemNameBlockItem> = REG.item("fossil") {
        FallacyItemNameBlockItem(FallacyBlocks.GEOLOGY.FOSSIL, it, ExtendedProperties.default())
    }.formattedLang().model(::withModel).tab(tabKey).register()

    private fun <T : Item> withModel(
        context: DataGenContext<Item, T>,
        provider: RegistrateItemModelProvider
    ) {
        provider.withExistingParent(context.name, Fallacy.id("block/geology/${context.name}"))
    }

    private val tabKey: ResourceKey<CreativeModeTab>
        get() = FallacyTabs.GEOLOGY.key!!
}