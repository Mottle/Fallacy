package dev.deepslate.fallacy.thermodynamics.data

import net.minecraft.world.level.LevelHeightAccessor
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

//用于存储Chunk每个位置中热量的目标值，不会存储
internal class ChunkHeatTarget(val heightAccessor: LevelHeightAccessor) {

    var afterLoading: AtomicBoolean = AtomicBoolean(false)

    internal data class HeatCounter(val heat: UInt, val sourceCount: Short)

    private class LayerTarget(val defaultHeat: UInt) {
        val heats: Array<Array<Array<HeatCounter>>> = Array(16) { _ ->
            Array(16) { _ ->
                Array(16) { _ ->
                    HeatCounter(defaultHeat, 1)
                }
            }
        }

        operator fun get(x: Int, y: Int, z: Int) = heats[x][y][z]
    }

    private val layers: Array<LayerTarget>

    init {
        val height = heightAccessor.height
        val size = height / 16 + if (height % 16 == 0) 0 else 1
        layers = Array(size) { LayerTarget(0u) }
    }

    operator fun get(x: Int, y: Int, z: Int): HeatCounter {
        val index = (y - heightAccessor.minBuildHeight + 1) / 16
        val layer = layers[index]
        return layer[x, y % 16, z]
    }
}