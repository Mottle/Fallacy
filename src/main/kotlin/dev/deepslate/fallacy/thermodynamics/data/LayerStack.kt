package dev.deepslate.fallacy.thermodynamics.data

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

class LayerStack(private val array: MutableList<HeatLayer>) {
    companion object {
        const val WORLD_DEEPEST = -64

        const val WORLD_HIGHEST = 320

        const val SIZE = (WORLD_HIGHEST - WORLD_DEEPEST) / HeatLayer.UNIT_COUNT

        private val eitherCodec: Codec<Either<String, HeatLayer>> = Codec.either(Codec.STRING, HeatLayer.CODEC)

        private fun to(either: Either<String, HeatLayer>): HeatLayer =
            if (either.left().isPresent) HeatLayer() else either.right().get()

        private fun from(layer: HeatLayer): Either<String, HeatLayer> =
            if (layer.isHomogeneous) Either.left("homogeneous") else Either.right(layer)

        private val fixedHeatLayerCodec: Codec<HeatLayer> = eitherCodec.xmap(::to, ::from)

        val CODEC: Codec<LayerStack> = RecordCodecBuilder.create { instance ->
            instance.group(fixedHeatLayerCodec.listOf().fieldOf("layers").forGetter(LayerStack::layers))
                .apply(instance, ::LayerStack)
        }

        private val eitherStreamCodec = ByteBufCodecs.either(ByteBufCodecs.STRING_UTF8, HeatLayer.STREAM_CODEC)

        private val fixedHeatLayerStreamCodec: StreamCodec<ByteBuf, HeatLayer> = eitherStreamCodec.map(::to, ::from)

        val STREAM_CODEC: StreamCodec<ByteBuf, LayerStack> =
            StreamCodec.composite(
                fixedHeatLayerStreamCodec.apply(ByteBufCodecs.list(24)),
                LayerStack::layers,
            ) { LayerStack(it) }
    }

    fun layers() = array.toList()
}