package dev.deepslate.fallacy.common.block.blocks

import com.tterrag.registrate.providers.DataGenContext
import com.tterrag.registrate.providers.RegistrateBlockstateProvider
import com.tterrag.registrate.util.entry.BlockEntry
import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.block.FallacyBlockTags
import dev.deepslate.fallacy.common.item.FallacyItems
import dev.deepslate.fallacy.common.registrate.REG
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.tags.BlockTags
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.minecraft.world.level.material.MapColor
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue
import net.neoforged.neoforge.client.model.generators.ConfiguredModel

object GeologyBlocks {
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

    val FOSSIL: BlockEntry<Block> =
        REG.block("fossil", ::Block).properties {
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.CLAY)
                .instrument(NoteBlockInstrument.XYLOPHONE)
                .requiresCorrectToolForDrops()
                .strength(4.5f)
                .sound(SoundType.BONE_BLOCK)
        }.blockstate(::withRock).loot { provider, self ->
            val fortune = provider.registries.lookup(Registries.ENCHANTMENT).get().get(Enchantments.FORTUNE).get()
            val drop = FallacyItems.MATERIAL.FOSSIL_FRAGMENT
            provider.add(
                self, LootTable.lootTable().apply(ApplyExplosionDecay.explosionDecay()).withPool(
                    LootPool.lootPool().setRolls(ConstantValue.exactly(1f)).add(
                        AlternativesEntry.alternatives(
                            LootItem.lootTableItem(drop).apply(
                                SetItemCountFunction.setCount(
                                    ConstantValue.exactly(1f)
                                )
                            )
                        )
                    )
                ).withPool(
                    LootPool.lootPool().setRolls(ConstantValue.exactly(1f)).add(
                        LootItem.lootTableItem(drop) // seeds additional
                            .apply(
                                ApplyBonusCount.addOreBonusCount(
                                    fortune
                                )
                            )
                    )
                )
            )
        }
            .defaultLang()
            .tag(
                BlockTags.MINEABLE_WITH_PICKAXE,
                BlockTags.BASE_STONE_OVERWORLD
            ).register()

    private fun <T : Block> withRock(
        context: DataGenContext<Block, T>,
        provider: RegistrateBlockstateProvider
    ) {
        val name = context.name
        val path = "block/geology/$name"
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
        val path = "block/geology/$name"
        val topBottomPath = "block/geology/${name}_top_bottom"
        val sideTexture = Fallacy.id(path)
        val topBottomTexture = Fallacy.id(topBottomPath)
        val model = provider.models().cubeColumn(path, sideTexture, topBottomTexture)
        val configuredModel = model.let(::ConfiguredModel)
        provider.getVariantBuilder(context.entry).partialState().setModels(configuredModel)

        val itemPath = BuiltInRegistries.BLOCK.getKey(context.entry).path
        provider.itemModels().getBuilder(itemPath).parent(model)
    }
}