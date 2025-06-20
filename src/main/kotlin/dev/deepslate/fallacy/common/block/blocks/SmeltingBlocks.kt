package dev.deepslate.fallacy.common.block.blocks

import com.tterrag.registrate.util.entry.BlockEntry
import dev.deepslate.fallacy.common.FallacyTabs
import dev.deepslate.fallacy.common.block.CrucibleBlock
import dev.deepslate.fallacy.common.block.FallacyBlocks
import dev.deepslate.fallacy.common.item.FallacyItemNameBlockItem
import dev.deepslate.fallacy.common.item.data.ExtendedProperties
import dev.deepslate.fallacy.common.registrate.REG
import dev.deepslate.fallacy.datagen.model.ModelHelper
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.material.MapColor

object SmeltingBlocks {
    val CRUCIBLE: BlockEntry<CrucibleBlock> = REG.block("crucible", ::CrucibleBlock).properties {
        Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(2.0F).noOcclusion()
    }.blockstate(ModelHelper.withUnknownTexture())
        .item { _, p -> FallacyItemNameBlockItem(FallacyBlocks.SMELTING.CRUCIBLE, p, ExtendedProperties.default()) }
        .properties { Item.Properties() }.tab(FallacyTabs.TOOL.key!!).build()
        .register()

//    val MOLTEN_COPPER: BlockEntry<LiquidBlock> =
//        REG.block("molten_copper") { p -> LiquidBlock(FallacyFluids.MOLTEN_COPPER.get(), p) }.properties {
//            Properties.ofFullCopy(Blocks.LAVA)
//        }.register()
//
//    val MOLTEN_LEAD: BlockEntry<LiquidBlock> =
//        REG.block("molten_lead") { p -> LiquidBlock(FallacyFluids.MOLTEN_LEAD.get(), p) }.properties {
//            Properties.ofFullCopy(Blocks.LAVA)
//        }.register()
//
//    val MOLTEN_IRON: BlockEntry<LiquidBlock> =
//        REG.block("molten_iron") { p -> LiquidBlock(FallacyFluids.MOLTEN_IRON.get(), p) }.properties {
//            Properties.ofFullCopy(Blocks.LAVA)
//        }.register()
}