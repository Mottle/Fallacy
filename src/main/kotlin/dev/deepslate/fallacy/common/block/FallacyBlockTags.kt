package dev.deepslate.fallacy.common.block

import dev.deepslate.fallacy.Fallacy
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

object FallacyBlockTags {
    //火成岩
    val IGNEOUS_ROCK: TagKey<Block> = BlockTags.create(Fallacy.withID("igneous_rock"))

    //变质岩
    val METAMORPHIC_ROCK: TagKey<Block> = BlockTags.create(Fallacy.withID("metamorphic_rock"))

    //沉积岩
    val SEDIMENTARY_ROCK: TagKey<Block> = BlockTags.create(Fallacy.withID("sedimentary_rock"))

    val COAL: TagKey<Block> = BlockTags.create(Fallacy.withID("coal"))

    val INCORRECT_FOR_DIGGER_LEVEL_0: TagKey<Block> = BlockTags.create(Fallacy.withID("incorrect_for_digger_level_0"))

    val INCORRECT_FOR_DIGGER_LEVEL_1: TagKey<Block> = BlockTags.create(Fallacy.withID("incorrect_for_digger_level_1"))

    val INCORRECT_FOR_DIGGER_LEVEL_2: TagKey<Block> = BlockTags.create(Fallacy.withID("incorrect_for_digger_level_2"))
}