package dev.deepslate.fallacy.util.region

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.RandomSource

object UniversalRegion : Region() {
    val CODEC = RecordCodecBuilder.mapCodec<UniversalRegion> { instance ->
        instance.group(Codec.INT.fieldOf("universal").forGetter { 0 }).apply(instance) { _ ->
            UniversalRegion
        }
    }

    override fun isIn(x: Int, y: Int, z: Int): Boolean = true

    override fun random(source: RandomSource): Triple<Int, Int, Int> = Triple(
        source.nextIntBetweenInclusive(-20000, 20000),
        source.nextIntBetweenInclusive(-20000, 20000),
        source.nextIntBetweenInclusive(-20000, 20000)
    )

    override val type: RegionType<out Region> = RegionTypes.UNIVERSAL
}