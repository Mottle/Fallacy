package dev.deepslate.fallacy.util.region

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import dev.deepslate.fallacy.Fallacy
import net.minecraft.core.Registry
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegistryBuilder

data class RegionType<R : Region>(val codec: MapCodec<R>, val streamCodec: StreamCodec<RegistryFriendlyByteBuf, R>) {
    @EventBusSubscriber(modid = Fallacy.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    companion object {
//        val CODEC_REGISTRY: Registry<RegionType<out Region>> = MappedRegistry(
//            ResourceKey.createRegistryKey<RegionType<out Region>>(Fallacy.id("region_type_codec")),
//            Lifecycle.stable()
//        )

//        private const val STREAM_CODEC_REGISTRY_KEY = "region_type_stream_codec"
//
//        val STREAM_CODEC_REGISTRY_ID = Fallacy.id(STREAM_CODEC_REGISTRY_KEY)

        val REGISTRY_KEY =
            ResourceKey.createRegistryKey<RegionType<out Region>>(Fallacy.withID("region_type"))

        val REGISTRY: Registry<RegionType<out Region>> =
            RegistryBuilder(REGISTRY_KEY).sync(true).create()

        val CODEC: Codec<RegionType<out Region>> = REGISTRY.byNameCodec()

        @SubscribeEvent
        fun onRegistry(event: NewRegistryEvent) {
            event.register(REGISTRY)
        }
    }
}