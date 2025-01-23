package dev.deepslate.fallacy.common.registrate

import com.tterrag.registrate.Registrate
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour

class ExtendedRegistrate(modId: String) : Registrate(modId) {
    fun <T : Block, P> extendedBlock(
        parent: P,
        name: String,
        factory: (BlockBehaviour.Properties) -> T
    ): ExtendedBlockBuilder<T, P> = entry(name) { callback ->
        ExtendedBlockBuilder.create(self(), parent, name, callback, factory)
    } as ExtendedBlockBuilder<T, P>

    fun <T : Block> extendedBlock(
        name: String,
        factory: (BlockBehaviour.Properties) -> T
    ): ExtendedBlockBuilder<T, Registrate> = extendedBlock(self(), name, factory)
}