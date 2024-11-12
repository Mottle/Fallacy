package dev.deepslate.fallacy.util.region

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import kotlin.math.absoluteValue

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

    init {
        require(chunkStart.x <= chunkEnd.x) { "Chunk start x must be less than or equal to chunk end x." }
        require(chunkStart.z <= chunkEnd.z) { "Chunk start z must be less than or equal to chunk end z." }
    }

    override fun isIn(x: Int, y: Int, z: Int): Boolean =
        x in chunkStart.minBlockX..chunkEnd.maxBlockX && z in chunkStart.minBlockZ..chunkEnd.maxBlockZ

    private val xStart = chunkStart.minBlockX

    private val xEnd = chunkStart.maxBlockX

    private val zStart = chunkStart.minBlockZ

    private val zEnd = chunkStart.maxBlockZ

    override fun random(level: Level): Triple<Int, Int, Int> {
        val source = level.random
        val randomX = source.nextInt(xStart, xEnd + 1)
        val randomZ = source.nextInt(zStart, zEnd + 1)
        val randomY = source.nextIntBetweenInclusive(level.minBuildHeight, level.maxBuildHeight)

        return Triple(randomX, randomY, randomZ)
    }

    override fun calculateVolume(level: Level): ULong {
        val dx = (chunkEnd.x - chunkStart.x).absoluteValue.toULong() + 1uL
        val dz = (chunkEnd.z - chunkStart.z).absoluteValue.toULong() + 1uL
        val dy = level.height.toULong()
        return dx * dy * dz
    }

    override val type: RegionType<out Region> = RegionTypes.CHUNK.get()
}