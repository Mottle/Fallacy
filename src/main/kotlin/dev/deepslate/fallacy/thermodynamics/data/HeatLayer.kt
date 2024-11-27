package dev.deepslate.fallacy.thermodynamics.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import java.util.*
import kotlin.jvm.optionals.getOrNull

internal class HeatLayer(private var data: ByteArray? = null) {

    companion object {
        //16 units per layer
        const val LAYER_UNIT_COUNT = 16

        //16 bit per unit
        const val UNIT_SIZE = 16

        const val STORAGE_SIZE = LAYER_UNIT_COUNT * LAYER_UNIT_COUNT * LAYER_UNIT_COUNT * UNIT_SIZE / 8

        const val MAX_HEAT = 0xffffu

        const val FREEZING_POINT = 273u

        val CODEC: Codec<HeatLayer> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.BYTE.listOf().optionalFieldOf("data").forGetter { Optional.ofNullable(it?.data?.toList()) })
                .apply(instance) { data ->
                    HeatLayer(data.getOrNull()?.toByteArray())
                }
        }

        val STREAM_CODEC: StreamCodec<ByteBuf, HeatLayer> =
            StreamCodec.composite(
                ByteBufCodecs.BYTE.apply(ByteBufCodecs.list()).apply(ByteBufCodecs::optional),
                { layer -> Optional.ofNullable(layer?.data?.toList()) }
            ) { data ->
                HeatLayer(data.getOrNull()?.toByteArray())
            }
    }

    fun init() {
        data = ByteArray(STORAGE_SIZE)
    }

    private fun getRawIndex(x: Int, y: Int, z: Int): Int {
        return (y shl 8) or (z shl 4) or x
    }

    val isEmpty: Boolean
        get() = data?.all { it == 0.toByte() } != false

    fun getHeat(x: Int, y: Int, z: Int): UInt {
        if (data == null) return FREEZING_POINT

        val index = getRawIndex(x, y, z)
        val high = data!![2 * index]
        val low = data!![2 * index + 1]
        return merge(high, low)
    }

    fun setHeat(x: Int, y: Int, z: Int, heat: UInt) {
        if (data == null) init()

        require(heat <= MAX_HEAT)
        val index = getRawIndex(x, y, z)
        val (high, low) = split(heat)
        data!![2 * index] = high
        data!![2 * index + 1] = low
    }

    operator fun get(x: Int, y: Int, z: Int): UInt = getHeat(x, y, z)

    operator fun set(x: Int, y: Int, z: Int, heat: UInt) {
        setHeat(x, y, z, heat)
    }

    fun copy(): HeatLayer = HeatLayer(data?.clone())

    private fun merge(high: Byte, low: Byte): UInt {
        return (high.toUInt() shl 8) or low.toUInt()
    }

    private fun split(value: UInt): Pair<Byte, Byte> {
        return ((value shr 8).toByte() to (value and 0xffu).toByte())
    }
}