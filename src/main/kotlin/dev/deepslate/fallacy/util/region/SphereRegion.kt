package dev.deepslate.fallacy.util.region

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.level.Level
import kotlin.math.*

data class SphereRegion(val centerX: Int, val centerY: Int, val centerZ: Int, val radius: Int) : Region() {
    companion object {
        val CODEC: MapCodec<SphereRegion> = RecordCodecBuilder.mapCodec<SphereRegion> { instance ->
            instance.group(
                Codec.INT.fieldOf("center_x").forGetter(SphereRegion::centerX),
                Codec.INT.fieldOf("center_y").forGetter(SphereRegion::centerY),
                Codec.INT.fieldOf("center_z").forGetter(SphereRegion::centerZ),
                Codec.INT.fieldOf("radius").forGetter(SphereRegion::radius)
            ).apply(instance, ::SphereRegion)
        }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SphereRegion> = StreamCodec.composite(
            ByteBufCodecs.INT, SphereRegion::centerX,
            ByteBufCodecs.INT, SphereRegion::centerY,
            ByteBufCodecs.INT, SphereRegion::centerZ,
            ByteBufCodecs.INT, SphereRegion::radius,
            ::SphereRegion
        )
    }

    init {
        require(radius > 0) { "Radius must be greater than 0." }
    }

    override fun contains(x: Int, y: Int, z: Int): Boolean {
        val dx = x - centerX
        val dy = y - centerY
        val dz = z - centerZ
        return dx * dx + dy * dy + dz * dz <= radius * radius * radius
    }

    override fun random(level: Level): Triple<Int, Int, Int> {
        val source = level.random
        val u = source.nextDouble()
        val v = source.nextDouble()
        val theta = 2 * PI * u
        val phi = acos(2 * v - 1)
        val r = radius * cbrt(source.nextDouble())

        val x = (centerX + r * sin(phi) * cos(theta)).toInt()
        val y = (centerY + r * sin(phi) * sin(theta)).toInt()
        val z = (centerZ + r * cos(phi)).toInt()
        val fixedY = y.coerceIn(level.minBuildHeight, level.maxBuildHeight)

        return Triple(x, fixedY, z)
    }

    override fun calculateVolume(level: Level): ULong {
        if (centerY !in level.minBuildHeight..level.maxBuildHeight)
            throw IllegalStateException("Center Y is out of bounds(${level.minBuildHeight}..${level.maxBuildHeight})")

        val sourceVolume = ((4.0 / 3.0) * PI * radius * radius * radius).toULong()
        if (centerY + radius <= level.maxBuildHeight && centerY - radius >= level.minBuildHeight) return sourceVolume
        val det1 = if (centerY + radius > level.maxBuildHeight) {
            val h = centerY + radius - level.maxBuildHeight
            (PI * h * h * (radius - h / 3)).toULong()
        } else 0uL

        val det2 = if (centerY - radius < level.minBuildHeight) {
            val h = level.minBuildHeight - (centerY - radius)
            (PI * h * h * (radius - h / 3)).toULong()
        } else 0uL

        return sourceVolume - det1 - det2
    }

    override val type: RegionType<out Region> = RegionTypes.SPHERE.get()
}