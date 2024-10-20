package dev.deepslate.fallacy.util.region

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.RandomSource
import net.minecraft.world.level.ChunkPos

data class ChunkRegion(val chunkStart: ChunkPos, val chunkEnd: ChunkPos) : Region() {

    companion object {
        val CODEC: MapCodec<ChunkRegion> = RecordCodecBuilder.mapCodec<ChunkRegion> { instance ->
            instance.group(
                Codec.LONG.fieldOf("chunk_start_packed_pos").forGetter { it.chunkStart.toLong() },
                Codec.LONG.fieldOf("chunk_end_packed_pos").forGetter { it.chunkEnd.toLong() })
                .apply(instance) { chunkStart, chunkEnd ->
                    ChunkRegion(ChunkPos(chunkStart), ChunkPos(chunkEnd))
                }
        }

        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ChunkRegion> = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, { region -> region.chunkStart.toLong() },
            ByteBufCodecs.VAR_LONG, { region -> region.chunkEnd.toLong() },
            { chunkStart, chunkEnd -> ChunkRegion(ChunkPos(chunkStart), ChunkPos(chunkEnd)) }
        )
    }

    override fun isIn(x: Int, y: Int, z: Int): Boolean =
        x in chunkStart.minBlockX..chunkEnd.maxBlockX && z in chunkStart.minBlockZ..chunkEnd.maxBlockZ

    private val xStart = chunkStart.minBlockX

    private val xEnd = chunkStart.maxBlockX

    private val zStart = chunkStart.minBlockZ

    private val zEnd = chunkStart.maxBlockZ

    override fun random(source: RandomSource): Triple<Int, Int, Int> {
        val randomX = source.nextInt(xStart, xEnd + 1)
        val randomZ = source.nextInt(zStart, zEnd + 1)
        val randomY = source.nextInt(-64, 320)

        return Triple(randomX, randomY, randomZ)
    }

    override val type: RegionType<out Region> = RegionTypes.CHUNK
}