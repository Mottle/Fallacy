package dev.deepslate.fallacy.common.registrate

import com.tterrag.registrate.builders.BlockBuilder
import dev.deepslate.fallacy.common.block.data.NPK
import dev.deepslate.fallacy.common.datapack.CropsConfiguration
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block

fun <T : Block, P> BlockBuilder<T, P>.npk(n: Int, p: Int, k: Int): BlockBuilder<T, P> = this.let {
    val namespace = owner.modid
    val namespacedId = ResourceLocation.fromNamespaceAndPath(namespace, name)
    val npk = NPK(n, p, k)

    CropsConfiguration.npk(namespacedId, npk)
    this
}

fun <T : Block, P> BlockBuilder<T, P>.celsius(min: Int, max: Int): BlockBuilder<T, P> = this.let {
    val namespace = owner.modid
    val namespacedId = ResourceLocation.fromNamespaceAndPath(namespace, name)

    CropsConfiguration.celsius(namespacedId, min, max)
    this
}

fun <T : Block, P> BlockBuilder<T, P>.brightness(min: Int, max: Int = Int.MAX_VALUE): BlockBuilder<T, P> = this.let {
    val namespace = owner.modid
    val namespacedId = ResourceLocation.fromNamespaceAndPath(namespace, name)

    CropsConfiguration.brightness(namespacedId, min, max)
    this
}