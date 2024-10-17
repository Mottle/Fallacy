package dev.deepslate.fallacy.util.region

import com.mojang.serialization.Codec
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.MapCodec
import dev.deepslate.fallacy.Fallacy
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey

data class RegionType<R : Region>(val codec: MapCodec<R>) {
    companion object {
        val REGISTRY: Registry<RegionType<out Region>> =
            MappedRegistry(ResourceKey.createRegistryKey(Fallacy.id("region_type")), Lifecycle.stable())

        val CODEC: Codec<RegionType<out Region>> = REGISTRY.byNameCodec()
    }
}