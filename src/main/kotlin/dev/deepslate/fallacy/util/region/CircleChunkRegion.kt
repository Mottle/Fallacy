package dev.deepslate.fallacy.util.region

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.util.RandomSource
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
    }

    override fun isIn(x: Int, y: Int, z: Int): Boolean {
        val dx = x - centerX
        val dz = z - centerZ
        return dx * dx + dz * dz <= radius * radius
    }

    override fun random(source: RandomSource): Triple<Int, Int, Int> {
        val randomRadius = source.nextInt(radius)
        val radian = source.nextDouble() * 2 * Math.PI
        val randomX = centerX + randomRadius * cos(radian).toInt()
        val randomZ = centerZ + randomRadius * sin(radian).toInt()
        val randomY = source.nextIntBetweenInclusive(-64, 320)

        return Triple(randomX, randomY, randomZ)
    }

    override val type: RegionType<out Region> = RegionTypes.CIRCLE_CHUNK
}