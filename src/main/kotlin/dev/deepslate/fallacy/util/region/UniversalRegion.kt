package dev.deepslate.fallacy.util.region

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.level.Level

object UniversalRegion : Region() {
    val CODEC = RecordCodecBuilder.mapCodec<UniversalRegion> { instance ->
        instance.group(Codec.INT.fieldOf("universal").forGetter { 0 }).apply(instance) { _ ->
            UniversalRegion
        }
    }

    val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, UniversalRegion> = StreamCodec.composite(
        ByteBufCodecs.INT, { 0 }, { UniversalRegion }
    )

    override fun contains(x: Int, y: Int, z: Int): Boolean = true

    override fun random(level: Level): Triple<Int, Int, Int> {
        val source = level.random
        val y = source.nextIntBetweenInclusive(level.minBuildHeight, level.maxBuildHeight)

        return Triple(
            source.nextIntBetweenInclusive(-20000, 20000),
            y,
            source.nextIntBetweenInclusive(-20000, 20000)
        )
    }

    override fun calculateVolume(level: Level): ULong = ULong.MAX_VALUE

    //非常隐蔽的坑点，UniversalRegion是一个object单例，在被引用前其变量不会初始化，故此处type若不为访问器则会在StreamCodec中为null -- 2024/10/21
    override val type: RegionType<out Region>
        get() = RegionTypes.UNIVERSAL.get()
}