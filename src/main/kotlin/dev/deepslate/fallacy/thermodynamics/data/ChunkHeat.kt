package dev.deepslate.fallacy.thermodynamics.data

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.core.BlockPos
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.level.LevelHeightAccessor

data class ChunkHeat(val layers: MutableList<LayerHeat?> = mutableListOf()) {
    companion object {
        private val eitherLayerCodec = Codec.either(Codec.INT, LayerHeat.CODEC)

        private fun to(either: Either<Int, LayerHeat>): LayerHeat? =
            if (either.left().isPresent) null else either.right().get()

        private fun from(layer: LayerHeat?): Either<Int, LayerHeat> =
            if (layer == null) Either.left(0) else Either.right(layer)

        private val nullableLayerHeatCodec: Codec<LayerHeat?> = eitherLayerCodec.xmap(::to, ::from)

        val CODEC: Codec<ChunkHeat> = RecordCodecBuilder.create { instance ->
            instance.group(nullableLayerHeatCodec.listOf().fieldOf("layers").forGetter(ChunkHeat::layers))
                .apply(instance, ::ChunkHeat)
        }

        private val eitherLayerStreamCodec: StreamCodec<ByteBuf, Either<Int, LayerHeat>> =
            ByteBufCodecs.either(ByteBufCodecs.INT, LayerHeat.STREAM_CODEC)

        private val nullableHeatLayerStreamCodec: StreamCodec<ByteBuf, LayerHeat?> =
            eitherLayerStreamCodec.map(::to, ::from)

        val STREAM_CODEC: StreamCodec<ByteBuf, ChunkHeat> =
            StreamCodec.composite(
                nullableHeatLayerStreamCodec.apply(ByteBufCodecs.list()),
                ChunkHeat::layers,
                ::ChunkHeat
            )
    }

    fun isInit() = !layers.isEmpty()

    fun init(levelHeightAccessor: LevelHeightAccessor) {
        val size = with(levelHeightAccessor) { maxBuildHeight - minBuildHeight } / 16 + 1
        for (i in 0 until size) {
            layers.add(null)
        }
    }

    private fun initLayer(index: Int): LayerHeat {
        layers[index] = LayerHeat()
        return layers[index]!!
    }

    fun getLayer(index: Int): LayerHeat = layers[index] ?: initLayer(index)

    fun setLayer(index: Int, layer: LayerHeat) {
        layers[index] = layer
    }

    private fun assertY(y: Int, access: LevelHeightAccessor) {
        require(y in access.minBuildHeight..access.maxBuildHeight) { "y[$y] out of bounds[${access.minBuildHeight}..${access.maxBuildHeight}]" }
    }

    fun getHeatBy(x: Int, y: Int, z: Int, access: LevelHeightAccessor): Float {
        assertY(y, access)
        val realY = y - access.minBuildHeight
        val index = (realY + 1) / LayerHeat.LAYER_LENGTH
        val layer = getLayer(index)
        return layer.getHeat(x % 16, realY % 16, z % 16)
    }

    fun getHeatBy(pos: BlockPos, access: LevelHeightAccessor): Float = getHeatBy(pos.x, pos.y, pos.z, access)

    fun getCountBy(x: Int, y: Int, z: Int, access: LevelHeightAccessor): UInt {
        assertY(y, access)
        val realY = y - access.minBuildHeight
        val index = (realY + 1) / LayerHeat.LAYER_LENGTH
        val layer = getLayer(index)
        return layer.getCount(x % 16, realY % 16, z % 16)
    }

    fun getCountBy(pos: BlockPos, access: LevelHeightAccessor): UInt = getCountBy(pos.x, pos.y, pos.z, access)

    fun setHeatBy(x: Int, y: Int, z: Int, heat: Float, access: LevelHeightAccessor) {
        assertY(y, access)
        val realY = y - access.minBuildHeight
        val index = (realY + 1) / LayerHeat.LAYER_LENGTH
        val layer = getLayer(index)
        layer.setHeat(x % 16, realY % 16, z % 16, heat)
    }

    fun setHeatBy(pos: BlockPos, heat: Float, access: LevelHeightAccessor) =
        setHeatBy(pos.x, pos.y, pos.z, heat, access)

    fun setCountBy(x: Int, y: Int, z: Int, count: UInt, access: LevelHeightAccessor) {
        assertY(y, access)
        val realY = y - access.minBuildHeight
        val index = (realY + 1) / LayerHeat.LAYER_LENGTH
        val layer = getLayer(index)
        layer.setCount(x % 16, realY % 16, z % 16, count)
    }

    fun add(x: Int, y: Int, z: Int, heat: Float, access: LevelHeightAccessor) {
        val count = getCountBy(x, y, z, access)
        val oldHeat = getHeatBy(x, y, z, access)

        if (count == 0u) {
            setHeatBy(x, y, z, heat, access)
            setCountBy(x, y, z, 1u, access)
            return
        }

        val newHeat = (oldHeat * count.toFloat() + heat) / (count.toFloat() + 1f)
        setHeatBy(x, y, z, newHeat, access)
        setCountBy(x, y, z, count + 1u, access)
    }

    fun add(pos: BlockPos, heat: Float, access: LevelHeightAccessor) = add(pos.x, pos.y, pos.z, heat, access)

    fun remove(x: Int, y: Int, z: Int, heat: Float, access: LevelHeightAccessor) {
        val count = getCountBy(x, y, z, access)
        val oldHeat = getHeatBy(x, y, z, access)

        if(count <= 1u) {
            setHeatBy(x, y, z, LayerHeat.UNCHECKED_SIMPLE, access)
            setCountBy(x, y, z, 0u, access)
            return
        }

        val newHeat = (oldHeat * count.toFloat() - heat) / (count.toFloat() - 1f)
        setHeatBy(x, y, z, newHeat, access)
        setCountBy(x, y, z, count - 1u, access)
    }

    fun remove(pos: BlockPos, heat: Float, access: LevelHeightAccessor) = remove(pos.x, pos.y, pos.z, heat, access)
}