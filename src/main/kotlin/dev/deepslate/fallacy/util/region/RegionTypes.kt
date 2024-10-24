package dev.deepslate.fallacy.util.region

import dev.deepslate.fallacy.Fallacy
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object RegionTypes {
    private val typeRegistry = DeferredRegister.create(RegionType.REGISTRY, Fallacy.MOD_ID)

    fun init(bus: IEventBus) {
        typeRegistry.register(bus)
    }

    private fun <R : Region> register(
        id: String,
        type: RegionType<R>
    ): DeferredHolder<RegionType<out Region>, RegionType<R>> = typeRegistry.register(id) { _ -> type }

    val CHUNK = register("chunk", RegionType(ChunkRegion.CODEC, ChunkRegion.STREAM_CODEC))

    val CIRCLE_CHUNK = register(
        "circle_chunk", RegionType(
            CircleChunkRegion.CODEC,
            CircleChunkRegion.STREAM_CODEC
        )
    )

    val CUBE = register("cube", RegionType(CubeRegion.CODEC, CubeRegion.STREAM_CODEC))

    val SPHERE = register("sphere", RegionType(SphereRegion.CODEC, SphereRegion.STREAM_CODEC))

    val UNIVERSAL = register(
        "universal", RegionType(
            UniversalRegion.CODEC,
            UniversalRegion.STREAM_CODEC
        )
    )

    val UNION = register("union", RegionType(UnionRegion.CODEC, UnionRegion.STREAM_CODEC))
}