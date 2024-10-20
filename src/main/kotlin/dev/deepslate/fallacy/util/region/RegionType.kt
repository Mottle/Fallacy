package dev.deepslate.fallacy.util.region

import com.mojang.serialization.Codec
import com.mojang.serialization.Lifecycle
import com.mojang.serialization.MapCodec
import dev.deepslate.fallacy.Fallacy
import net.minecraft.core.MappedRegistry
import net.minecraft.core.Registry
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey

data class RegionType<R : Region>(val codec: MapCodec<R>, val streamCodec: StreamCodec<RegistryFriendlyByteBuf, R>) {
    companion object {
        val CODEC_REGISTRY: Registry<RegionType<out Region>> =
            MappedRegistry(ResourceKey.createRegistryKey(Fallacy.id("region_type_codec")), Lifecycle.stable())

        val CODEC: Codec<RegionType<out Region>> = CODEC_REGISTRY.byNameCodec()

        private const val STREAM_CODEC_REGISTRY_KEY = "region_type_stream_codec"

        val STREAM_CODEC_REGISTRY_ID = Fallacy.id(STREAM_CODEC_REGISTRY_KEY)
    }
}