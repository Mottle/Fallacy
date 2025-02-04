package dev.deepslate.fallacy.util.region

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.level.Level
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

data class CircleChunkRegion(val centerX: Int, val centerZ: Int, val radius: Int) : Region() {
    companion object {
        val CODEC: MapCodec<CircleChunkRegion> = RecordCodecBuilder.mapCodec<CircleChunkRegion> { instance ->
            instance.group(
                Codec.INT.fieldOf("center_x").forGetter(CircleChunkRegion::centerX),
                Codec.INT.fieldOf("center_z").forGetter(CircleChunkRegion::centerZ),
                Codec.INT.fieldOf("radius").forGetter(CircleChunkRegion::radius)
            ).apply(instance, ::CircleChunkRegion)
        }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, CircleChunkRegion> = StreamCodec.composite(
            ByteBufCodecs.INT, CircleChunkRegion::centerX,
            ByteBufCodecs.INT, CircleChunkRegion::centerZ,
            ByteBufCodecs.INT, CircleChunkRegion::radius,
            ::CircleChunkRegion
        )
    }

    init {
        require(radius > 0) { "radius must be greater than 0." }
    }

    override fun contains(x: Int, y: Int, z: Int): Boolean {
        val dx = x - centerX
        val dz = z - centerZ
        return dx * dx + dz * dz <= radius * radius
    }

    override fun random(level: Level): Triple<Int, Int, Int> {
        val source = level.random
        val randomRadius = source.nextInt(radius)
        val radian = source.nextDouble() * 2 * Math.PI
        val randomX = centerX + randomRadius * cos(radian).toInt()
        val randomZ = centerZ + randomRadius * sin(radian).toInt()
        val randomY = source.nextIntBetweenInclusive(level.minBuildHeight, level.maxBuildHeight)

        return Triple(randomX, randomY, randomZ)
    }

    override fun calculateVolume(level: Level): ULong {
        val dy = level.height.toULong()
        val area = (0.5 * PI * radius * radius).toULong()
        return dy * area
    }

    override val type: RegionType<out Region> = RegionTypes.CIRCLE_CHUNK.get()
}