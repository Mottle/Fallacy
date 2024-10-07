package dev.deepslate.fallacy.thermodynamics.data

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

class LayerStack(private val array: List<HeatLayer> = listOf()) {
    companion object {
        const val WORLD_DEEPEST = -64

        const val WORLD_HIGHEST = 320

        const val SIZE = (WORLD_HIGHEST - WORLD_DEEPEST) / HeatLayer.UNIT_COUNT

        private val eitherCodec: Codec<Either<Int, HeatLayer>> = Codec.either(Codec.INT, HeatLayer.CODEC)

        private fun to(either: Either<Int, HeatLayer>): HeatLayer =
            if (either.left().isPresent) HeatLayer() else either.right().get()

        private fun from(layer: HeatLayer): Either<Int, HeatLayer> =
            if (layer.isEmpty) Either.left(0) else Either.right(layer)

        private val fixedHeatLayerCodec: Codec<HeatLayer> = eitherCodec.xmap(::to, ::from)

        val CODEC: Codec<LayerStack> = RecordCodecBuilder.create { instance ->
            instance.group(fixedHeatLayerCodec.listOf().fieldOf("layers").forGetter(LayerStack::layers))
                .apply(instance, ::LayerStack)
        }

        private val eitherStreamCodec: StreamCodec<ByteBuf, Either<Int, HeatLayer>> =
            ByteBufCodecs.either(ByteBufCodecs.INT, HeatLayer.STREAM_CODEC)

        private val fixedHeatLayerStreamCodec: StreamCodec<ByteBuf, HeatLayer> = eitherStreamCodec.map(::to, ::from)

        val STREAM_CODEC: StreamCodec<ByteBuf, LayerStack> =
            StreamCodec.composite(
                fixedHeatLayerStreamCodec.apply(ByteBufCodecs.list()),
                LayerStack::layers,
            ) { LayerStack(it) }
    }

    fun layers() = array.toList()
}