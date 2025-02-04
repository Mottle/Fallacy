package dev.deepslate.fallacy.util.region

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.level.Level

class UnionRegion(val union: List<Region> = emptyList()) : Region() {

    companion object {
        val CODEC: MapCodec<UnionRegion> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Region.CODEC.listOf().fieldOf("union").forGetter { it.union }
            ).apply(instance, ::UnionRegion)
        }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, UnionRegion> = StreamCodec.composite(
            Region.STREAM_CODEC.apply(ByteBufCodecs.list()), UnionRegion::union,
            ::UnionRegion
        )
    }

    init {
        require(union.none { it is UniversalRegion }) { "UnionRegion cannot contain UniversalRegion!" }
    }

    override fun contains(x: Int, y: Int, z: Int): Boolean = union.any { it.contains(x, y, z) }

    override fun random(level: Level): Triple<Int, Int, Int> {
//        val unionWithVolume = union.map { it to it.calculateVolume(level) }
//        val totalVolume = unionWithVolume.sumOf { it.second }
//        val volumes = unionWithVolume.fold(emptyList<ULong>()) { acc, (_, volume) -> acc + acc.last() + volume }
//        val volumeWithRange = unionWithVolume.zip(volumes).map { (p, start) -> p.first to (start .. start + p.second) }
        var totalVolume = 0uL
        val ranges = mutableListOf<Pair<ULongRange, Region>>()
        for (region in union) {
            val volume = region.calculateVolume(level)
            val range = totalVolume..totalVolume + volume - 1uL
            ranges.add(range to region)
            totalVolume += volume
        }
        val random = (0uL..totalVolume - 1uL).random()
        val region = ranges.first { it.first.contains(random) }.second

        return region.random(level)
    }

    override fun calculateVolume(level: Level): ULong = union.map { it.calculateVolume(level) }.sum()

    override val type: RegionType<out Region> = RegionTypes.UNION.get()
}