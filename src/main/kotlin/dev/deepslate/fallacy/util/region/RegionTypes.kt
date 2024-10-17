package dev.deepslate.fallacy.util.region

import dev.deepslate.fallacy.Fallacy
import net.minecraft.core.Registry

object RegionTypes {
    private fun <R : Region> register(id: String, type: RegionType<R>): RegionType<R> =
        Registry.register(RegionType.REGISTRY, Fallacy.id(id), type)

    val CHUNK: RegionType<ChunkRegion> = register("chunk", RegionType(ChunkRegion.CODEC))

    val CIRCLE_CHUNK: RegionType<CircleChunkRegion> = register("circle_chunk", RegionType(CircleChunkRegion.CODEC))

    val CUBE: RegionType<CubeRegion> = register("cube", RegionType(CubeRegion.CODEC))

    val SPHERE: RegionType<SphereRegion> = register("sphere", RegionType(SphereRegion.CODEC))
}