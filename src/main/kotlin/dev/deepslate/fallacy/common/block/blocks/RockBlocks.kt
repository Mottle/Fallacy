package dev.deepslate.fallacy.common.block.blocks

import com.tterrag.registrate.providers.DataGenContext
import com.tterrag.registrate.providers.RegistrateBlockstateProvider
import com.tterrag.registrate.util.entry.BlockEntry
import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.block.FallacyBlockTags
import dev.deepslate.fallacy.common.registrate.REG
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.neoforged.neoforge.client.model.generators.ConfiguredModel

object RockBlocks {
    private val defaultProperties =
        BlockBehaviour.Properties.of().strength(1.5f, 6f).instrument(NoteBlockInstrument.BASEDRUM)
            .requiresCorrectToolForDrops()

    //片麻岩
    val GNEISS: BlockEntry<Block> =
        REG.block("gneiss", ::Block).properties { defaultProperties }.blockstate(::withRock).defaultLoot().defaultLang()
            .tag(
                BlockTags.MINEABLE_WITH_PICKAXE,
                BlockTags.BASE_STONE_OVERWORLD,
                FallacyBlockTags.METAMORPHIC_ROCK
            ).register()

    //大理岩
    val MARBLE: BlockEntry<Block> =
        REG.block("marble", ::Block).properties { defaultProperties.strength(3f, 8f) }.blockstate(::withRock)
            .defaultLoot().defaultLang()
            .tag(
                BlockTags.MINEABLE_WITH_PICKAXE,
                BlockTags.BASE_STONE_OVERWORLD,
                FallacyBlockTags.METAMORPHIC_ROCK
            ).register()

    //片岩
    val SCHIST: BlockEntry<Block> =
        REG.block("schist", ::Block).properties { defaultProperties }.blockstate(::withRock).defaultLoot().defaultLang()
            .tag(
                BlockTags.MINEABLE_WITH_PICKAXE,
                BlockTags.BASE_STONE_OVERWORLD,
                FallacyBlockTags.METAMORPHIC_ROCK
            ).register()

    //石英岩
    val QUARTZITE: BlockEntry<Block> =
        REG.block("quartzite", ::Block).properties { defaultProperties }.blockstate(::withRock).defaultLoot()
            .defaultLang()
            .tag(
                BlockTags.MINEABLE_WITH_PICKAXE,
                BlockTags.BASE_STONE_OVERWORLD,
                FallacyBlockTags.METAMORPHIC_ROCK
            ).register()

    //矽卡岩
    val SKARN: BlockEntry<Block> =
        REG.block("skarn", ::Block).properties { defaultProperties }.blockstate(::withRock).defaultLoot().defaultLang()
            .tag(
                BlockTags.MINEABLE_WITH_PICKAXE,
                BlockTags.BASE_STONE_OVERWORLD,
                FallacyBlockTags.METAMORPHIC_ROCK
            ).register()

    //千枚岩
    val PHYLLITE: BlockEntry<Block> =
        REG.block("phyllite", ::Block).properties { defaultProperties }.blockstate(::withRock).defaultLoot()
            .defaultLang()
            .tag(
                BlockTags.MINEABLE_WITH_PICKAXE,
                BlockTags.BASE_STONE_OVERWORLD,
                FallacyBlockTags.METAMORPHIC_ROCK
            ).register()

    //页岩
    val SHALE: BlockEntry<Block> =
        REG.block("shale", ::Block).properties { defaultProperties }.blockstate(::withRock).defaultLoot()
            .defaultLang()
            .tag(
                BlockTags.MINEABLE_WITH_PICKAXE,
                BlockTags.BASE_STONE_OVERWORLD,
                FallacyBlockTags.SEDIMENTARY_ROCK
            ).register()

    //烁岩
    val CONGLOMERATE: BlockEntry<Block> =
        REG.block("conglomerate", ::Block).properties { defaultProperties }.blockstate(::withRock).defaultLoot()
            .defaultLang()
            .tag(
                BlockTags.MINEABLE_WITH_PICKAXE,
                BlockTags.BASE_STONE_OVERWORLD,
                FallacyBlockTags.SEDIMENTARY_ROCK
            ).register()

    //泥炭
    val PEAT: BlockEntry<Block> =
        REG.block("peat", ::Block).properties { defaultProperties }.blockstate(::withRock).defaultLoot()
            .defaultLang()
            .tag(
                BlockTags.MINEABLE_WITH_PICKAXE,
                BlockTags.BASE_STONE_OVERWORLD,
                FallacyBlockTags.SEDIMENTARY_ROCK
            ).register()

    //石灰岩
    val LIMESTONE: BlockEntry<Block> =
        REG.block("limestone", ::Block).properties { defaultProperties }.blockstate(::withTopBottomTextureRock)
            .defaultLoot()
            .defaultLang()
            .tag(
                BlockTags.MINEABLE_WITH_PICKAXE,
                BlockTags.BASE_STONE_OVERWORLD,
                FallacyBlockTags.SEDIMENTARY_ROCK
            ).register()

    //岩盐
    val HALITE: BlockEntry<Block> =
        REG.block("halite", ::Block).properties { defaultProperties }.blockstate(::withRock).defaultLoot()
            .defaultLang()
            .tag(
                BlockTags.MINEABLE_WITH_PICKAXE,
                BlockTags.BASE_STONE_OVERWORLD,
                FallacyBlockTags.SEDIMENTARY_ROCK
            ).register()

    private fun <T : Block> withRock(
        context: DataGenContext<Block, T>,
        provider: RegistrateBlockstateProvider
    ) {
        val name = context.name
        val path = "block/rock/$name"
        val texture = Fallacy.id(path)
        val model = provider.models().cubeAll(path, texture)
        val configuredModel = model.let(::ConfiguredModel)
        provider.getVariantBuilder(context.entry).partialState().setModels(configuredModel)

        val itemPath = BuiltInRegistries.BLOCK.getKey(context.entry).path
        provider.itemModels().getBuilder(itemPath).parent(model)
    }

    private fun <T : Block> withTopBottomTextureRock(
        context: DataGenContext<Block, T>,
        provider: RegistrateBlockstateProvider
    ) {
        val name = context.name
        val path = "block/rock/$name"
        val topBottomPath = "block/rock/${name}_top_bottom"
        val sideTexture = Fallacy.id(path)
        val topBottomTexture = Fallacy.id(topBottomPath)
        val model = provider.models().cubeColumn(path, sideTexture, topBottomTexture)
        val configuredModel = model.let(::ConfiguredModel)
        provider.getVariantBuilder(context.entry).partialState().setModels(configuredModel)

        val itemPath = BuiltInRegistries.BLOCK.getKey(context.entry).path
        provider.itemModels().getBuilder(itemPath).parent(model)
    }
}