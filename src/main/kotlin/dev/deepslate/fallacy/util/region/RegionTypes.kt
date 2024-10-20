package dev.deepslate.fallacy.util.region

import dev.deepslate.fallacy.Fallacy
import jdk.internal.org.jline.reader.LineReader
import net.minecraft.core.Registry

object RegionTypes {
    private fun <R : Region> register(id: String, type: RegionType<R>): RegionType<R> =
        Registry.register(RegionType.CODEC_REGISTRY, Fallacy.id(id), type)

    val CHUNK: RegionType<ChunkRegion> = register("chunk", RegionType(ChunkRegion.CODEC, ChunkRegion.STREAM_CODEC))

    val CIRCLE_CHUNK: RegionType<CircleChunkRegion> = register(
        "circle_chunk", RegionType(
            CircleChunkRegion.CODEC,
            CircleChunkRegion.STREAM_CODEC
        )
    )

    val CUBE: RegionType<CubeRegion> = register("cube", RegionType(CubeRegion.CODEC, CubeRegion.STREAM_CODEC))

    val SPHERE: RegionType<SphereRegion> = register("sphere", RegionType(SphereRegion.CODEC, SphereRegion.STREAM_CODEC))

    val UNIVERSAL: RegionType<UniversalRegion> = register(
        "universal", RegionType(
            UniversalRegion.CODEC,
            UniversalRegion.STREAM_CODEC
        )
    )
}