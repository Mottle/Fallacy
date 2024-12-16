package dev.deepslate.fallacy.thermodynamics.data

import net.minecraft.core.BlockPos
import net.minecraft.world.level.LevelHeightAccessor

//用于存储Chunk每个位置中热量的目标值，不会存储
internal class HeatUpdateCache(val heightAccessor: LevelHeightAccessor) {

    companion object {
        fun getIndex(y: Int, access: LevelHeightAccessor) = (y - access.minBuildHeight + 1) / 16

        fun getMaxSize(access: LevelHeightAccessor) = access.height / 16 + 1

        private val UNCHECKED_HEAT_FLOAT = HeatLayer.UNCHECKED_HEAT.toFloat()

        private fun fixInput(v: Int): Int = (v % 16 + 16) % 16
    }

    //权衡float与ushort，即使ushort可以节省2bytes，但是float带来的精度提升是不可代替的，使用float可以简单的实现方块温度影响的计算与移除
    data class HeatCache(val heat: Float, val sourceCount: UShort) {
        companion object {
            fun of(heat: UInt) = HeatCache(heat.toFloat(), 1u)

            fun empty() = HeatCache(UNCHECKED_HEAT_FLOAT, 0u)
        }

        infix operator fun plus(value: UInt) = if (isUnchecked()) of(value) else
            HeatCache(
                calculatePlus(value),
                (sourceCount + 1u).toUShort()
            )

        infix operator fun minus(value: UInt) = if (sourceCount <= 1u.toUShort()) empty() else
            HeatCache(
                calculateMinus(value),
                (sourceCount - 1u).toUShort()
            )

        private fun calculatePlus(value: UInt) =
            (heat * sourceCount.toFloat() + value.toFloat()) / (sourceCount.toFloat() + 1f)

        private fun calculateMinus(value: UInt) =
            (heat * sourceCount.toFloat() - value.toFloat()) / (sourceCount.toFloat() - 1f)

        fun isUnchecked() = sourceCount == 0u.toUShort()

        infix fun merge(other: HeatCache): HeatCache {
            if (isUnchecked()) return other
            if (other.isUnchecked()) return this

            val sum = heat * sourceCount.toFloat()
            val otherSum = other.heat * other.sourceCount.toFloat()

            return HeatCache(
                (sum + otherSum) / (sourceCount.toFloat() + other.sourceCount.toFloat()),
                (sourceCount + other.sourceCount).toUShort()
            )
        }
    }

    private class LayerCache(defaultHeat: UInt) {

        companion object {
            fun empty() = LayerCache(HeatLayer.UNCHECKED_HEAT)
        }

        class CacheBox(defaultHeat: UInt) {
            private val data: Array<Array<Array<HeatCache>>>

            init {
                val default =
                    if (defaultHeat == HeatLayer.UNCHECKED_HEAT) HeatCache.empty() else HeatCache.of(defaultHeat)
                data = Array(16) { _ ->
                    Array(16) { _ ->
                        Array(16) { _ ->
                            default
                        }
                    }
                }
            }

            operator fun get(x: Int, y: Int, z: Int) = data[x][y][z]

            operator fun set(x: Int, y: Int, z: Int, value: HeatCache) {
                data[x][y][z] = value
            }

            infix fun merge(other: CacheBox): CacheBox {
                val new = CacheBox(HeatLayer.UNCHECKED_HEAT)
                for (x in 0..15) for (y in 0..15) for (z in 0..15) {
                    new[x, y, z] = this[x, y, z] merge other[x, y, z]
                }
                return new
            }
        }

        private val data: Array<CacheBox?> = Array(9) { null }

        init {
            data[4] = CacheBox(defaultHeat)
        }

        fun getLeftUp() = data[0]

        fun getUp() = data[1]

        fun getRightUp() = data[2]

        fun getLeft() = data[3]

        fun getRight() = data[5]

        fun getLeftDown() = data[6]

        fun getDown() = data[7]

        fun getRightDown() = data[8]

        fun getSelf() = data[4]!!

        private fun getIndex(x: Int, z: Int): Int {
            val remapX = x + 16
            val remapZ = z + 16
            val dataXIndex = (remapX + 1) / 16
            val dataZIndex = (remapZ + 1) / 16
            val index = dataZIndex * 3 + dataXIndex

            return index
        }

        operator fun get(x: Int, y: Int, z: Int): HeatCache {
            val index = getIndex(x, z)

            val box = data[index] ?: return HeatCache.empty()
            return box[fixInput(x) % 16, fixInput(y), fixInput(z) % 16]
        }

        operator fun set(x: Int, y: Int, z: Int, value: HeatCache) {
            val index = getIndex(x, z)

            if (data[index] == null) data[index] = CacheBox(HeatLayer.UNCHECKED_HEAT)

            val box = data[index]!!
            box[fixInput(x), fixInput(y), fixInput(z)] = value
        }

        fun toHeatLayer(aroundCacheBox: List<CacheBox>) =
            aroundCacheBox.fold(getSelf()) { sum, next -> sum merge next }.let {
                val layer = HeatLayer()
                for (x in 0 until 16) for (y in 0 until 16) for (z in 0 until 16) {
                    layer[x, y, z] = it[x, y, z].heat.toUInt()
                }
                return@let layer
            }
    }

    private val layers: Array<LayerCache?>

    init {
        val height = heightAccessor.height
        val size = height / 16 + 1
        layers = Array(size) { null }
    }

    fun getIndex(y: Int) = getIndex(y, heightAccessor)

    fun getMaxSize() = getMaxSize(heightAccessor)

    private fun initLayer(index: Int) {
        layers[index] = LayerCache.empty()
    }

    operator fun get(x: Int, y: Int, z: Int): HeatCache {
        val index = getIndex(y)
        if (layers[index] == null) initLayer(index)
        val layer = layers[index]!!
        return layer[fixInput(x), fixInput(y), fixInput(z)]
    }

    operator fun set(x: Int, y: Int, z: Int, value: HeatCache) {
        val index = getIndex(y)
        if (layers[index] == null) initLayer(index)
        val layer = layers[index]!!
        layer[fixInput(x), fixInput(y), fixInput(z)] = value
    }

    operator fun get(pos: BlockPos): HeatCache = get(pos.x, pos.y, pos.z)

    operator fun set(pos: BlockPos, value: HeatCache) {
        set(pos.x, pos.y, pos.z, value)
    }

    fun toLayerStack(
        leftUp: HeatUpdateCache?,
        up: HeatUpdateCache?,
        rightUp: HeatUpdateCache?,
        left: HeatUpdateCache?,
        right: HeatUpdateCache?,
        leftDown: HeatUpdateCache?,
        down: HeatUpdateCache?,
        rightDown: HeatUpdateCache?
    ): LayerStack {
        val empty = List(getMaxSize()) { null }
        val leftUpCacheBoxes = leftUp?.layers?.map { it?.getRightDown() } ?: empty
        val upCacheBoxes = up?.layers?.map { it?.getDown() } ?: empty
        val rightUpCacheBoxes = rightUp?.layers?.map { it?.getLeftDown() } ?: empty
        val leftCacheBoxes = left?.layers?.map { it?.getRight() } ?: empty
        val rightCacheBoxes = right?.layers?.map { it?.getLeft() } ?: empty
        val leftDownCacheBoxes = leftDown?.layers?.map { it?.getRightUp() } ?: empty
        val downCacheBoxes = down?.layers?.map { it?.getUp() } ?: emptyList()
        val rightDownCacheBoxes = rightDown?.layers?.map { it?.getLeftUp() } ?: empty
        val cacheBoxes4Layer = mutableListOf<List<LayerCache.CacheBox>>()

        for (id in 0 until layers.size) {
            val list = mutableListOf<LayerCache.CacheBox?>()
            list.add(leftUpCacheBoxes[id])
            list.add(upCacheBoxes[id])
            list.add(rightUpCacheBoxes[id])
            list.add(leftCacheBoxes[id])
            list.add(rightCacheBoxes[id])
            list.add(leftDownCacheBoxes[id])
            list.add(downCacheBoxes[id])
            list.add(rightDownCacheBoxes[id])
            cacheBoxes4Layer.add(list.filterNotNull())
        }

        val stack = layers.map { it ?: LayerCache.empty() }.zip(cacheBoxes4Layer)
            .map { (layerCache, caches) -> layerCache.toHeatLayer(caches) }
            .let(LayerStack::from)
        return stack
    }
}