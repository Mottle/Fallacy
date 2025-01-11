package dev.deepslate.fallacy.thermodynamics.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

class LayerHeat(
    defaultImpacts: Array<Float> = Array(LAYER_SIZE) { UNCHECKED_SIMPLE },
    defaultCounts: Array<UShort> = Array(LAYER_SIZE) { 0u }
) {
    companion object {
        const val MAX_HEAT = 65535f //0xffff

        const val MIN_HEAT = 0f

        const val FREEZING_POINT = 273f

        const val UNCHECKED_SIMPLE = MAX_HEAT + 1f

        const val LAYER_LENGTH = 16

        const val LAYER_SIZE = LAYER_LENGTH * LAYER_LENGTH * LAYER_LENGTH

        fun isUnchecked(heat: Float) = heat > MAX_HEAT

        val CODEC: Codec<LayerHeat> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.FLOAT.listOf().fieldOf("data").forGetter { it.heatImpacts.toList() },
                Codec.SHORT.listOf().fieldOf("counts").forGetter { it.heatImpactCounts.map { u -> u.toShort() } }
            ).apply(instance) { data, counts ->
                LayerHeat(data.toTypedArray(), counts.map { it.toUShort() }.toTypedArray())
            }
        }

        val STREAM_CODEC: StreamCodec<ByteBuf, LayerHeat> =
            StreamCodec.composite(
                ByteBufCodecs.FLOAT.apply(ByteBufCodecs.list()),
                { layer -> layer.heatImpacts.toList() },
                ByteBufCodecs.SHORT.apply(ByteBufCodecs.list()),
                { layer -> layer.heatImpactCounts.map { u -> u.toShort() } }
            ) { data, counts ->
                LayerHeat(data.toTypedArray(), counts.map { it.toUShort() }.toTypedArray())
            }
    }

    private val heatImpacts: Array<Float> = defaultImpacts

    private val heatImpactCounts: Array<UShort> = defaultCounts

    fun getHeat(x: Int, y: Int, z: Int): Float = heatImpacts[(x * LAYER_LENGTH * LAYER_LENGTH) + (y * LAYER_LENGTH) + z]

    fun setHeat(x: Int, y: Int, z: Int, heat: Float) {
        heatImpacts[(x * LAYER_LENGTH * LAYER_LENGTH) + (y * LAYER_LENGTH) + z] = heat
    }

    fun getCount(x: Int, y: Int, z: Int): UInt =
        heatImpactCounts[(x * LAYER_LENGTH * LAYER_LENGTH) + (y * LAYER_LENGTH) + z].toUInt()

    fun setCount(x: Int, y: Int, z: Int, count: UInt) {
        heatImpactCounts[(x * LAYER_LENGTH * LAYER_LENGTH) + (y * LAYER_LENGTH) + z] = count.toUShort()
    }

    fun isEmpty() = heatImpacts.all {
        isUnchecked(it)
    }
}