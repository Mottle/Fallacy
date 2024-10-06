package dev.deepslate.fallacy.common.block

import com.tterrag.registrate.util.entry.BlockEntry
import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.block.crop.DyingCropBlock
import dev.deepslate.fallacy.common.block.crop.FallacyCropBlock
import dev.deepslate.fallacy.common.block.data.NPK
import dev.deepslate.fallacy.common.item.FallacyItems
import dev.deepslate.fallacy.common.registrate.REG
import dev.deepslate.fallacy.common.registrate.formattedLang
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.BlockTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FarmBlock
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.neoforged.neoforge.client.model.generators.ConfiguredModel

object FallacyBlocks {

    init {
        Crop
    }

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

    val FARMLAND: BlockEntry<NPKFarmBlock> = REG.block("farmland", ::NPKFarmBlock).properties {
        Properties.ofFullCopy(Blocks.FARMLAND)
    }.blockstate { ctx, prov ->
        prov.getVariantBuilder(ctx.entry).forAllStates { state ->
            val moisture = state.getValue(FarmBlock.MOISTURE)
            val name = if (moisture != 7) "farmland" else "farmland_moist"
            val stateModel = prov.models().getExistingFile(prov.mcLoc("block/$name"))
            return@forAllStates ConfiguredModel.builder().modelFile(stateModel).build()
        }
    }.formattedLang().register()

    object Crop {
        private val defaultTags = arrayOf(
            BlockTags.CROPS, BlockTags.MAINTAINS_FARMLAND,
            BlockTags.MINEABLE_WITH_AXE
        )

        private val defaultProperties = Properties.ofFullCopy(Blocks.WHEAT)

        val DYING_CROP = REG.block("dying_crop", ::DyingCropBlock).properties { Properties.ofFullCopy(Blocks.WHEAT) }
            .blockstate { ctx, prov ->
                prov.simpleBlock(
                    ctx.get(),
                    prov.models().cross("block/crop/dying_crop", Fallacy.id("block/crop/dying_crop"))
                        .renderType("cutout")
                )
            }.formattedLang().tag(BlockTags.CROPS, BlockTags.MAINTAINS_FARMLAND)
            .register()

        val WHEAT = REG.block("wheat") { FallacyCropBlock(it, NPK(0, 0, 0)) }.properties { defaultProperties }
            .blockstate(FallacyCropBlock::withVanillaBlockStack)
            .loot(
                FallacyCropBlock.withLoot(
                    BuiltInRegistries.ITEM.getHolder(BuiltInRegistries.ITEM.getKey(Items.WHEAT)).get(),
                    (1..3),
                    (1..3)
                )
            )
            .tag(*defaultTags).formattedLang().register()

        val POTATOES = REG.block("potatoes") { FallacyCropBlock(it, NPK(0, 0, 0)) }.properties { defaultProperties }
            .blockstate(FallacyCropBlock::withVanillaBlockStateAlt).loot(
                FallacyCropBlock.withLoot(
                    FallacyItems.Crop.POTATO,
                    (1..3),
                    (1..3)
                )
            ).tag(*defaultTags).formattedLang().register()

        val CARROTS = REG.block("carrots") { FallacyCropBlock(it, NPK(0, 0, 0)) }.properties { defaultProperties }
            .blockstate(FallacyCropBlock::withVanillaBlockStateAlt).loot(
                FallacyCropBlock.withLoot(
                    FallacyItems.Crop.CARROT,
                    (1..3),
                    (1..3)
                )
            ).tag(*defaultTags).formattedLang().register()

        val BEETROOTS = REG.block("beetroots") { FallacyCropBlock(it, NPK(0, 0, 0)) }.properties { defaultProperties }
            .blockstate(FallacyCropBlock::withVanillaBlockStateAlt).loot(
                FallacyCropBlock.withLoot(
                    BuiltInRegistries.ITEM.getHolder(BuiltInRegistries.ITEM.getKey(Items.BEETROOT)).get(),
                    (1..3),
                    (1..3)
                )
            ).tag(*defaultTags).formattedLang().register()

        val BARLEY = REG.block("barley") { FallacyCropBlock(it, NPK(0, 0, 0)) }.properties { defaultProperties }
            .blockstate(FallacyCropBlock::withBlockState)
            .loot(FallacyCropBlock.withLoot(FallacyItems.Crop.BARLEY, (1..3), (1..4)))
            .tag(*defaultTags).formattedLang().register()

        val OAT = REG.block("oat") { FallacyCropBlock(it, NPK(0, 0, 0)) }.properties { defaultProperties }
            .blockstate(FallacyCropBlock::withBlockState)
            .loot(FallacyCropBlock.withLoot(FallacyItems.Crop.OAT, (1..3), (1..4)))
            .tag(*defaultTags).formattedLang().register()

        val SOYBEAN = REG.block("soybean") { FallacyCropBlock(it, NPK(1, 1, 1)) }.properties { defaultProperties }
            .blockstate(FallacyCropBlock::withBlockState)
            .loot(FallacyCropBlock.withNoSeedsLoot(FallacyItems.Crop.SOYBEAN, (2..8)))
            .tag(*defaultTags).formattedLang().register()

        val TOMATO = REG.block("tomato") { FallacyCropBlock(it, NPK(0, 0, 0)) }.properties { defaultProperties }
            .blockstate(FallacyCropBlock::withBlockState)
            .loot(FallacyCropBlock.withLoot(FallacyItems.Crop.TOMATO, (1..3), (1..3)))
            .tag(*defaultTags).formattedLang().register()

        val SPINACH = REG.block("spinach") { FallacyCropBlock(it, NPK(0, 0, 0)) }.properties { defaultProperties }
            .blockstate(FallacyCropBlock::withBlockState)
            .loot(FallacyCropBlock.withLoot(FallacyItems.Crop.SPINACH, (1..3), (1..3)))
            .tag(*defaultTags).formattedLang().register()
    }
}























