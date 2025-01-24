package dev.deepslate.fallacy.common.block

import com.tterrag.registrate.Registrate
import com.tterrag.registrate.builders.BlockBuilder
import com.tterrag.registrate.util.entry.BlockEntry
import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.block.crop.DyingCropBlock
import dev.deepslate.fallacy.common.block.crop.FallacyCropBlock
import dev.deepslate.fallacy.common.item.FallacyItems
import dev.deepslate.fallacy.common.registrate.REG
import dev.deepslate.fallacy.common.registrate.formattedLang
import dev.deepslate.fallacy.common.registrate.npk
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.BlockTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FarmBlock
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.material.PushReaction
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

    object Crop {
        private val defaultTags = arrayOf(
            BlockTags.CROPS, BlockTags.MAINTAINS_FARMLAND,
            BlockTags.MINEABLE_WITH_AXE
        )

        private val defaultProperties = Properties.ofFullCopy(Blocks.WHEAT)

        private val noRandomTickProperties = Properties.of()
            .mapColor(MapColor.PLANT)
            .noCollission()
            .instabreak()
            .sound(SoundType.CROP)
            .pushReaction(PushReaction.DESTROY)

        val DYING_CROP: BlockEntry<DyingCropBlock> =
            REG.block("dying_crop", ::DyingCropBlock).properties { noRandomTickProperties }
                .blockstate { ctx, prov ->
                    prov.simpleBlock(
                        ctx.get(),
                        prov.models().cross("block/crop/dying_crop", Fallacy.id("block/crop/dying_crop"))
                            .renderType("cutout")
                    )
                }.formattedLang().tag(BlockTags.CROPS, BlockTags.MAINTAINS_FARMLAND)
                .register()

        val WHEAT: BlockEntry<FallacyCropBlock> =
            REG.block("wheat", ::FallacyCropBlock).properties { defaultProperties }
                .blockstate(FallacyCropBlock::withVanillaBlockStack)
                .npk(2, 2, 2)
                .loot(
                    FallacyCropBlock.withLoot(
                        BuiltInRegistries.ITEM.getHolder(BuiltInRegistries.ITEM.getKey(Items.WHEAT)).get(),
                        (1..3),
                        (1..3)
                    )
                )
                .tag(*defaultTags).formattedLang().register()

        val POTATOES: BlockEntry<FallacyCropBlock> =
            REG.block("potatoes", ::FallacyCropBlock).properties { defaultProperties }
                .blockstate(FallacyCropBlock::withVanillaBlockStateAlt)
                .npk(2, 1, 2)
                .loot(
                    FallacyCropBlock.withLoot(
                        FallacyItems.Crop.POTATO,
                        (1..3),
                        (1..3)
                    )
                ).tag(*defaultTags).formattedLang().register()

        val CARROTS: BlockEntry<FallacyCropBlock> =
            REG.block("carrots", ::FallacyCropBlock).properties { defaultProperties }
                .blockstate(FallacyCropBlock::withVanillaBlockStateAlt)
                .npk(1, 2, 2)
                .loot(
                    FallacyCropBlock.withLoot(
                        FallacyItems.Crop.CARROT,
                        (1..3),
                        (1..3)
                    )
                ).tag(*defaultTags).formattedLang().register()

        val BEETROOTS: BlockEntry<FallacyCropBlock> =
            REG.block("beetroots", ::FallacyCropBlock).properties { defaultProperties }
                .blockstate(FallacyCropBlock::withVanillaBlockStateAlt)
                .npk(2, 3, 3)
                .loot(
                    FallacyCropBlock.withLoot(
                        BuiltInRegistries.ITEM.getHolder(BuiltInRegistries.ITEM.getKey(Items.BEETROOT)).get(),
                        (1..3),
                        (1..3)
                    )
                ).tag(*defaultTags).formattedLang().register()

        //大麦
        val BARLEY: BlockEntry<FallacyCropBlock> =
            crop("barley", { FallacyItems.Crop.BARLEY }, seedsRange = 1..3).npk(3, 3, 3).register()

        //燕麦
        val OAT: BlockEntry<FallacyCropBlock> =
            crop("oat", { FallacyItems.Crop.OAT }, seedsRange = 1..4).npk(1, 0, 1).register()

        //大豆
        val SOYBEAN: BlockEntry<FallacyCropBlock> =
            seedCrop("soybean", { FallacyItems.Crop.SOYBEAN }, (2..8)).npk(2, 2, 1).register()

        val TOMATO: BlockEntry<FallacyCropBlock> = crop("tomato", { FallacyItems.Crop.TOMATO }).npk(3, 2, 2).register()

        //菠菜
        val SPINACH: BlockEntry<FallacyCropBlock> =
            crop("spinach", { FallacyItems.Crop.SPINACH }).npk(1, 2, 1).register()

        //辣椒
        val CHILE_PEPPER: BlockEntry<FallacyCropBlock> =
            crop("chile_pepper", { FallacyItems.Crop.CHILE_PEPPER }).npk(2, 2, 2).register()

        val CORN: BlockEntry<FallacyCropBlock> = crop("corn", { FallacyItems.Crop.CORN }).npk(2, 3, 1).register()

        val EGGPLANT: BlockEntry<FallacyCropBlock> =
            crop("eggplant", { FallacyItems.Crop.EGGPLANT }).npk(4, 3, 2).register()

        //芦笋
        val ASPARAGUS: BlockEntry<FallacyCropBlock> =
            crop("asparagus", { FallacyItems.Crop.ASPARAGUS }).npk(2, 4, 3).register()

        //芹菜
        val CELERY: BlockEntry<FallacyCropBlock> = crop("celery", { FallacyItems.Crop.CELERY }).npk(3, 2, 3).register()

        val CABBAGE: BlockEntry<FallacyCropBlock> =
            crop("cabbage", { FallacyItems.Crop.CABBAGE }).npk(1, 1, 2).register()

        private fun crop(
            name: String,
            gain: () -> Holder<Item>,
            gainRange: IntRange = 1..3,
            seedsRange: IntRange = 1..3
        ): BlockBuilder<FallacyCropBlock, Registrate> =
            REG.block(name, ::FallacyCropBlock).properties { defaultProperties }
                .blockstate(FallacyCropBlock::withBlockState)
                .loot(FallacyCropBlock.withLoot(gain(), gainRange, seedsRange))
                .tag(*defaultTags).formattedLang()

        private fun seedCrop(
            name: String,
            gain: () -> Holder<Item>,
            gainRange: IntRange
        ): BlockBuilder<FallacyCropBlock, Registrate> =
            REG.block(name, ::FallacyCropBlock).properties { defaultProperties }
                .blockstate(FallacyCropBlock::withBlockState)
                .loot(FallacyCropBlock.withNoSeedsLoot(gain(), gainRange))
                .tag(*defaultTags).formattedLang()
    }
}