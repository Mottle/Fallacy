package dev.deepslate.fallacy.common.data

import com.tterrag.registrate.util.entry.BlockEntry
import dev.deepslate.fallacy.common.block.MiuBerryBushBlock
import dev.deepslate.fallacy.common.registrate.REG
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.neoforged.neoforge.client.model.generators.ConfiguredModel

object FallacyBlocks {
    val MIU_BERRY_BUSH: BlockEntry<MiuBerryBushBlock> = REG
        .block<MiuBerryBushBlock>("miu_berry_bush", ::MiuBerryBushBlock).properties {
            Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.SWEET_BERRY_BUSH)
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
        }
        .tag(
            BlockTags.BEE_GROWABLES,
            BlockTags.FALL_DAMAGE_RESETTING,
            BlockTags.MINEABLE_WITH_AXE,
            BlockTags.SWORD_EFFICIENT
        ).lang("Miu Berry Bush").register()
}























