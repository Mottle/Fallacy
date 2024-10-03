package dev.deepslate.fallacy.common.block

import com.tterrag.registrate.util.entry.BlockEntry
import dev.deepslate.fallacy.common.item.FallacyItems
import dev.deepslate.fallacy.common.registrate.REG
import net.minecraft.advancements.critereon.StatePropertiesPredicate
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.neoforged.neoforge.client.model.generators.ConfiguredModel

object FallacyBlocks {
    val MIU_BERRY_BUSH: BlockEntry<MiuBerryBushBlock> = REG
        .block<MiuBerryBushBlock>("miu_berry_bush", ::MiuBerryBushBlock).properties {
            Properties.ofFullCopy(Blocks.SWEET_BERRY_BUSH)
        }
        .blockstate { ctx, prov ->
            prov.getVariantBuilder(ctx.entry).forAllStates { state ->
                val age = state.getValue(MiuBerryBushBlock.AGE)
                val name = "${ctx.name}_stage$age"
//                    val stateModel = prov.models().cross(name, prov.modLoc("block/nature/$name")).renderType(RenderType.CUTOUT.name)
                val stateModel = prov.models().getExistingFile(prov.modLoc("block/$name"))

                return@forAllStates ConfiguredModel.builder().modelFile(stateModel)
                    .build()
            }
        }.loot { prov, block ->
            prov.add(
                block, LootTable.lootTable().withPool(
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
        ).lang("Miu Berry Bush").register()
}























