package dev.deepslate.fallacy.common.block

import dev.deepslate.fallacy.Fallacy
import net.minecraft.tags.BlockTags
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

object FallacyBlockTags {
    //火成岩
    val IGNEOUS_ROCK: TagKey<Block> = BlockTags.create(Fallacy.id("igneous_rock"))

    //变质岩
    val METAMORPHIC_ROCK: TagKey<Block> = BlockTags.create(Fallacy.id("metamorphic_rock"))

    //沉积岩
    val SEDIMENTARY_ROCK: TagKey<Block> = BlockTags.create(Fallacy.id("sedimentary_rock"))

    val COAL: TagKey<Block> = BlockTags.create(Fallacy.id("coal"))
}