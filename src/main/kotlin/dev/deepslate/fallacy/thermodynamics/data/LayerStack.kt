package dev.deepslate.fallacy.thermodynamics.data

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

//Chunk持有的数据结构，用于存储此Chunk中对应位置的当前温度
internal class LayerStack(private val data: MutableList<HeatLayer> = mutableListOf()) {
    companion object {
//        const val WORLD_DEEPEST = -64
//
//        const val WORLD_HIGHEST = 320

//        const val SIZE = (WORLD_HIGHEST - WORLD_DEEPEST) / HeatLayer.LAYER_UNIT_COUNT

        private val eitherCodec: Codec<Either<Int, HeatLayer>> = Codec.either(Codec.INT, HeatLayer.CODEC)

        private fun to(either: Either<Int, HeatLayer>): HeatLayer =
            if (either.left().isPresent) HeatLayer() else either.right().get()

        private fun from(layer: HeatLayer): Either<Int, HeatLayer> =
            if (layer.isEmpty) Either.left(0) else Either.right(layer)

        private val fixedHeatLayerCodec: Codec<HeatLayer> = eitherCodec.xmap(::to, ::from)

        val CODEC: Codec<LayerStack> = RecordCodecBuilder.create { instance ->
            instance.group(fixedHeatLayerCodec.listOf().fieldOf("layers").forGetter(LayerStack::data))
                .apply(instance, ::LayerStack)
        }

        private val eitherStreamCodec: StreamCodec<ByteBuf, Either<Int, HeatLayer>> =
            ByteBufCodecs.either(ByteBufCodecs.INT, HeatLayer.STREAM_CODEC)

        private val fixedHeatLayerStreamCodec: StreamCodec<ByteBuf, HeatLayer> = eitherStreamCodec.map(::to, ::from)

        val STREAM_CODEC: StreamCodec<ByteBuf, LayerStack> =
            StreamCodec.composite(
                fixedHeatLayerStreamCodec.apply(ByteBufCodecs.list()),
                LayerStack::data,
            ) { LayerStack(it) }
    }

    fun getLayer(index: Int): HeatLayer = data[index]

    fun setLayer(index: Int, layer: HeatLayer) {
        data[index] = layer
    }
}