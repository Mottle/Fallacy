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
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.BlockTags
import net.minecraft.world.item.Item
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

        val DYING_CROP: BlockEntry<DyingCropBlock> =
            REG.block("dying_crop", ::DyingCropBlock).properties { Properties.ofFullCopy(Blocks.WHEAT) }
                .blockstate { ctx, prov ->
                    prov.simpleBlock(
                        ctx.get(),
                        prov.models().cross("block/crop/dying_crop", Fallacy.id("block/crop/dying_crop"))
                            .renderType("cutout")
                    )
                }.formattedLang().tag(BlockTags.CROPS, BlockTags.MAINTAINS_FARMLAND)
                .register()

        val WHEAT: BlockEntry<FallacyCropBlock> =
            REG.block("wheat") { FallacyCropBlock(it, NPK(0, 0, 0)) }.properties { defaultProperties }
                .blockstate(FallacyCropBlock::withVanillaBlockStack)
                .loot(
                    FallacyCropBlock.withLoot(
                        BuiltInRegistries.ITEM.getHolder(BuiltInRegistries.ITEM.getKey(Items.WHEAT)).get(),
                        (1..3),
                        (1..3)
                    )
                )
                .tag(*defaultTags).formattedLang().register()

        val POTATOES: BlockEntry<FallacyCropBlock> =
            REG.block("potatoes") { FallacyCropBlock(it, NPK(0, 0, 0)) }.properties { defaultProperties }
                .blockstate(FallacyCropBlock::withVanillaBlockStateAlt).loot(
                    FallacyCropBlock.withLoot(
                        FallacyItems.Crop.POTATO,
                        (1..3),
                        (1..3)
                    )
                ).tag(*defaultTags).formattedLang().register()

        val CARROTS: BlockEntry<FallacyCropBlock> =
            REG.block("carrots") { FallacyCropBlock(it, NPK(0, 0, 0)) }.properties { defaultProperties }
                .blockstate(FallacyCropBlock::withVanillaBlockStateAlt).loot(
                    FallacyCropBlock.withLoot(
                        FallacyItems.Crop.CARROT,
                        (1..3),
                        (1..3)
                    )
                ).tag(*defaultTags).formattedLang().register()

        val BEETROOTS: BlockEntry<FallacyCropBlock> =
            REG.block("beetroots") { FallacyCropBlock(it, NPK(0, 0, 0)) }.properties { defaultProperties }
                .blockstate(FallacyCropBlock::withVanillaBlockStateAlt).loot(
                    FallacyCropBlock.withLoot(
                        BuiltInRegistries.ITEM.getHolder(BuiltInRegistries.ITEM.getKey(Items.BEETROOT)).get(),
                        (1..3),
                        (1..3)
                    )
                ).tag(*defaultTags).formattedLang().register()

        val BARLEY: BlockEntry<FallacyCropBlock> =
            crop("barley", NPK(0, 0, 0), { FallacyItems.Crop.BARLEY }, seedsRange = 1..3)

        val OAT: BlockEntry<FallacyCropBlock> = crop("oat", NPK(0, 0, 0), { FallacyItems.Crop.OAT }, seedsRange = 1..4)

        val SOYBEAN: BlockEntry<FallacyCropBlock> =
            seedCrop("soybean", NPK(0, 0, 0), { FallacyItems.Crop.SOYBEAN }, (2..8))

        val TOMATO: BlockEntry<FallacyCropBlock> = crop("tomato", NPK(0, 0, 0), { FallacyItems.Crop.TOMATO })

        val SPINACH: BlockEntry<FallacyCropBlock> = crop("spinach", NPK(0, 0, 0), { FallacyItems.Crop.SPINACH })

        val CHILE_PEPPER: BlockEntry<FallacyCropBlock> =
            crop("chile_pepper", NPK(0, 0, 0), { FallacyItems.Crop.CHILE_PEPPER })

        val CORN: BlockEntry<FallacyCropBlock> = crop("corn", NPK(0, 0, 0), { FallacyItems.Crop.CORN })

        val EGGPLANT: BlockEntry<FallacyCropBlock> = crop("eggplant", NPK(0, 0, 0), { FallacyItems.Crop.EGGPLANT })

        val ASPARAGUS: BlockEntry<FallacyCropBlock> = crop("asparagus", NPK(0, 0, 0), { FallacyItems.Crop.ASPARAGUS })

        val CELERY: BlockEntry<FallacyCropBlock> = crop("celery", NPK(0, 0, 0), { FallacyItems.Crop.CELERY })

        val CABBAGE: BlockEntry<FallacyCropBlock> = crop("cabbage", NPK(0, 0, 0), { FallacyItems.Crop.CABBAGE })

        private fun crop(
            name: String,
            npkRequire: NPK,
            gain: () -> Holder<Item>,
            gainRange: IntRange = 1..3,
            seedsRange: IntRange = 1..3
        ): BlockEntry<FallacyCropBlock> =
            REG.block(name) { FallacyCropBlock(it, npkRequire) }.properties { defaultProperties }
                .blockstate(FallacyCropBlock::withBlockState)
                .loot(FallacyCropBlock.withLoot(gain(), gainRange, seedsRange))
                .tag(*defaultTags).formattedLang().register()

        private fun seedCrop(
            name: String,
            npkRequire: NPK,
            gain: () -> Holder<Item>,
            gainRange: IntRange
        ): BlockEntry<FallacyCropBlock> =
            REG.block(name) { FallacyCropBlock(it, npkRequire) }.properties { defaultProperties }
                .blockstate(FallacyCropBlock::withBlockState)
                .loot(FallacyCropBlock.withNoSeedsLoot(gain(), gainRange))
                .tag(*defaultTags).formattedLang().register()
    }
}