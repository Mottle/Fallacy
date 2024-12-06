package dev.deepslate.fallacy.thermodynamics.data

import net.minecraft.core.BlockPos
import net.minecraft.world.level.LevelHeightAccessor
import java.util.concurrent.atomic.AtomicBoolean

//用于存储Chunk每个位置中热量的目标值，不会存储
internal class ChunkHeatTarget(val heightAccessor: LevelHeightAccessor) {

    var loadFinished: AtomicBoolean = AtomicBoolean(false)

    internal data class HeatCounter(val heat: UShort, val sourceCount: UShort)

    private class LayerTarget(defaultHeat: UShort) {
        val heats: Array<Array<Array<HeatCounter>>> = Array(16) { _ ->
            Array(16) { _ ->
                Array(16) { _ ->
                    HeatCounter(defaultHeat, 1u)
                }
            }
        }

        operator fun get(x: Int, y: Int, z: Int) = heats[x][y][z]

        operator fun set(x: Int, y: Int, z: Int, value: HeatCounter) {
            heats[x][y][z] = value
        }

        fun toHeatLayer(): HeatLayer {
            val layer = HeatLayer()
            for (x in 0 until 16) for (y in 0 until 16) for (z in 0 until 16) {
                layer[x, y, z] = heats[x][y][z].heat.toUInt()
            }
            return layer
        }
    }

    private val layers: Array<LayerTarget?>

    init {
        val height = heightAccessor.height
        val size = height / 16 + 1
        layers = Array(size) { null }
    }

    fun getIndex(y: Int) = (y - heightAccessor.minBuildHeight + 1) / 16

    fun getMaxSize() = heightAccessor.height / 16 + 1

    private fun initLayer(index: Int) {
        layers[index] = LayerTarget(0u)
    }

    operator fun get(x: Int, y: Int, z: Int): HeatCounter {
        val index = getIndex(y)
        if (layers[index] == null) initLayer(index)
        val layer = layers[index]!!
        return layer[fixInput(x), fixInput(y), fixInput(z)]
    }

    operator fun set(x: Int, y: Int, z: Int, value: HeatCounter) {
        val index = getIndex(y)
        if (layers[index] == null) initLayer(index)
        val layer = layers[index]!!
        layer[fixInput(x), fixInput(y), fixInput(z)] = value
    }

    operator fun get(pos: BlockPos): HeatCounter = get(pos.x, pos.y, pos.z)

    operator fun set(pos: BlockPos, value: HeatCounter) {
        set(pos.x, pos.y, pos.z, value)
    }

    private fun fixInput(v: Int): Int = (v % 16 + 16) % 16

    fun toLayerStack(): LayerStack = LayerStack(layers.map {
        it?.toHeatLayer() ?: HeatLayer()
    }.toMutableList())
}