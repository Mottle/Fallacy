package dev.deepslate.fallacy.util.region

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.RandomSource
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.cbrt
import kotlin.math.cos
import kotlin.math.sin

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

    override fun isIn(x: Int, y: Int, z: Int): Boolean {
        val dx = x - centerX
        val dy = y - centerY
        val dz = z - centerZ
        return dx * dx + dy * dy + dz * dz <= radius * radius * radius
    }

    override fun random(source: RandomSource): Triple<Int, Int, Int> {
        val u = source.nextDouble()
        val v = source.nextDouble()
        val theta = 2 * PI * u
        val phi = acos(2 * v - 1)
        val r = radius * cbrt(source.nextDouble())

        val x = (centerX + r * sin(phi) * cos(theta)).toInt()
        val y = (centerY + r * sin(phi) * sin(theta)).toInt()
        val z = (centerZ + r * cos(phi)).toInt()

        return Triple(x, y, z)
    }

    override val type: RegionType<out Region> = RegionTypes.SPHERE
}