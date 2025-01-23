package dev.deepslate.fallacy.common.registrate

import com.tterrag.registrate.AbstractRegistrate
import com.tterrag.registrate.builders.BlockBuilder
import com.tterrag.registrate.builders.BuilderCallback
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour

class ExtendedBlockBuilder<T : Block, P>(
    owner: AbstractRegistrate<*>,
    parent: P,
    name: String,
    callback: BuilderCallback,
    factory: (BlockBehaviour.Properties) -> T,
    init: () -> BlockBehaviour.Properties
) : BlockBuilder<T, P>(owner, parent, name, callback, factory, init) {

    companion object {
        fun <T : Block, P> create(
            owner: AbstractRegistrate<*>,
            parent: P,
            name: String,
            callback: BuilderCallback,
            factory: (BlockBehaviour.Properties) -> T
        ): ExtendedBlockBuilder<T, P> =
            ExtendedBlockBuilder(owner, parent, name, callback, factory, BlockBehaviour.Properties::of)
    }
}