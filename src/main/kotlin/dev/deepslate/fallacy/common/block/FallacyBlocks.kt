package dev.deepslate.fallacy.common.block

import com.tterrag.registrate.util.entry.BlockEntry
import dev.deepslate.fallacy.common.block.blocks.CropBlocks
import dev.deepslate.fallacy.common.block.blocks.GeologyBlocks
import dev.deepslate.fallacy.common.block.blocks.SmeltingBlocks
import dev.deepslate.fallacy.common.item.FallacyItems
import dev.deepslate.fallacy.common.registrate.REG
import dev.deepslate.fallacy.common.registrate.formattedLang
import dev.deepslate.fallacy.datagen.model.ModelHelper
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FarmBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.neoforged.neoforge.client.model.generators.ConfiguredModel

object FallacyBlocks {

    val MIU_BERRY_BUSH: BlockEntry<MiuBerryBushBlock> = REG
        .block("miu_berry_bush", ::MiuBerryBushBlock).properties {
            Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH)
        }
        .blockstate { ctx, prov ->
            prov.getVariantBuilder(ctx.entry).forAllStates { state ->
                val age = state.getValue(MiuBerryBushBlock.AGE)
                val name = "${ctx.name}_stage$age"
                val stateModel = prov.models().getExistingFile(prov.modLoc("block/$name"))

                return@forAllStates ConfiguredModel.builder().modelFile(stateModel).build()
            }
        }.loot { prov, block ->
            prov.add(
                block, LootTable.lootTable().apply(ApplyExplosionDecay.explosionDecay()).withPool(
                    LootPool.lootPool().setRolls(ConstantValue.exactly(1f)).add(
                        LootItem.lootTableItem(FallacyItems.MIU_BERRIES).apply(
                            SetItemCountFunction.setCount(ConstantValue.exactly(1f))
                        ).`when`(
                            LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(
                                StatePropertiesPredicate.Builder.properties().hasProperty(MiuBerryBushBlock.AGE, 3)
                            ).or(
                                LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(
                                    StatePropertiesPredicate.Builder.properties().hasProperty(MiuBerryBushBlock.AGE, 2)
                                )
                            ).or(
                                LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).setProperties(
                                    StatePropertiesPredicate.Builder.properties().hasProperty(MiuBerryBushBlock.AGE, 1)
                                )
                            )
                        )
                    )
                )
            )
        }
        .tag(
            BlockTags.BEE_GROWABLES,
            BlockTags.FALL_DAMAGE_RESETTING,
            BlockTags.MINEABLE_WITH_AXE,
            BlockTags.SWORD_EFFICIENT
        ).formattedLang().register()

    val FARMLAND: BlockEntry<FertilityFarmBlock> = REG.block("farmland", ::FertilityFarmBlock).properties {
        Properties.ofFullCopy(Blocks.FARMLAND)
    }.blockstate { ctx, prov ->
        prov.getVariantBuilder(ctx.entry).forAllStates { state ->
            val moisture = state.getValue(FarmBlock.MOISTURE)
            val name = if (moisture != 7) "farmland" else "farmland_moist"
            val stateModel = prov.models().getExistingFile(prov.mcLoc("block/$name"))
            return@forAllStates ConfiguredModel.builder().modelFile(stateModel).build()
        }
    }.formattedLang().register()

    val BURNING_LOG: BlockEntry<BurningLog> = REG.block("burning_log", ::BurningLog).properties {
        Properties.of().strength(2f).instrument(NoteBlockInstrument.BASS).sound(
            SoundType.WOOD
        ).lightLevel { _ -> 6 }.mapColor(MapColor.WOOD).requiresCorrectToolForDrops().noLootTable()
    }.blockstate(ModelHelper.withTexture("charcoal_forge/lit")).tag(BlockTags.MINEABLE_WITH_PICKAXE).register()

    val BURNOUT_LOG: BlockEntry<Block> = REG.block("burnout_log", ::Block).properties {
        Properties.of().strength(0.5f).instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD)
            .mapColor(MapColor.COLOR_BLACK).sound(SoundType.SAND)
    }.blockstate(ModelHelper.withTexture("charcoal_forge/ash"))
        .tag(FallacyBlockTags.COAL, BlockTags.MINEABLE_WITH_SHOVEL).register()

    val CROP = CropBlocks

    val GEOLOGY = GeologyBlocks

    val SMELTING = SmeltingBlocks
}