package dev.deepslate.fallacy.util.region

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.RandomSource

data class CubeRegion(val xStart: Int, val yStart: Int, val zStart: Int, val xEnd: Int, val yEnd: Int, val zEnd: Int) :
    Region() {
    companion object {
        val CODEC: MapCodec<CubeRegion> = RecordCodecBuilder.mapCodec<CubeRegion> { instance ->
            instance.group(
                Codec.INT.fieldOf("x_start").forGetter(CubeRegion::xStart),
                Codec.INT.fieldOf("y_start").forGetter(CubeRegion::yStart),
                Codec.INT.fieldOf("z_start").forGetter(CubeRegion::zStart),
                Codec.INT.fieldOf("x_end").forGetter(CubeRegion::xEnd),
                Codec.INT.fieldOf("y_end").forGetter(CubeRegion::yEnd),
                Codec.INT.fieldOf("z_end").forGetter(CubeRegion::zEnd)
            ).apply(instance, ::CubeRegion)
        }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, CubeRegion> = StreamCodec.composite(
            ByteBufCodecs.INT, CubeRegion::xStart,
            ByteBufCodecs.INT, CubeRegion::yStart,
            ByteBufCodecs.INT, CubeRegion::zStart,
            ByteBufCodecs.INT, CubeRegion::xEnd,
            ByteBufCodecs.INT, CubeRegion::yEnd,
            ByteBufCodecs.INT, CubeRegion::zEnd,
            ::CubeRegion
        )
    }

    override fun isIn(x: Int, y: Int, z: Int): Boolean = x in xStart..xEnd && y in yStart..yEnd && z in zStart..zEnd

    override fun random(source: RandomSource): Triple<Int, Int, Int> {
        val randomX = source.nextInt(xStart, xEnd + 1)
        val randomY = source.nextInt(yStart, yEnd + 1)
        val randomZ = source.nextInt(zStart, zEnd + 1)
        return Triple(randomX, randomY, randomZ)
    }

    override val type: RegionType<out Region> = RegionTypes.CUBE.get()
}