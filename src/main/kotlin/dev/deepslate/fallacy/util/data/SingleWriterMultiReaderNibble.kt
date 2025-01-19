package dev.deepslate.fallacy.util.data

import java.util.BitSet

abstract class SingleWriterMultiReaderNibble(data: ByteArray? = null) {

    abstract fun getUnitBitSize(): Int

    abstract fun getMaxSize(): Int

    fun getMaxBitSet() = getMaxSize() * getUnitBitSize()

    protected var writeableData: BitSet = if(data == null) BitSet(getMaxBitSet()) else BitSet.valueOf(data)

    protected var readableData: BitSet = writeableData.clone() as BitSet

    protected fun getAt(index: Int, data: BitSet): Int {
        val startIndex = index * getUnitBitSize()
        val endIndex = startIndex + getUnitBitSize() - 1
        var value = 0
        for(i in endIndex downTo startIndex) {
            value *= 2
            value += if(data.get(i)) 1 else 0
        }
        return value
    }

    protected fun setAt(index: Int, value: Int, data: BitSet) {
        val startIndex = index * getUnitBitSize()
        val endIndex = startIndex + getUnitBitSize() - 1
        var v = value
        for(i in startIndex..endIndex) {
            data.set(i, v % 2 == 1)
            v /= 2
        }
    }

    private fun fixInput(value: Int) = ((value % 16) + 16) % 16

    fun getIndex(x: Int, y: Int, z: Int) = y.let(::fixInput) * 256 + x.let(::fixInput) * 16 + z.let(::fixInput)

    fun set(index: Int, value: Int) {
        setAt(index, value, writeableData)
    }

    fun set(x: Int, y: Int, z: Int, value: Int) {
        set(getIndex(x, y, z), value)
    }

    fun getWriteable(index: Int): Int {
        return getAt(index, writeableData)
    }

    fun getWriteable(x: Int, y: Int, z: Int): Int = getWriteable(getIndex(x, y, z))

    fun getReadable(index: Int): Int = getAt(index, readableData)

    fun getReadable(x: Int, y: Int, z: Int): Int = getReadable(getIndex(x, y, z))

    fun update() {
        synchronized(this) {
            readableData = writeableData.clone() as BitSet
        }
    }

    private fun isAll(bitSet: BitSet, start: Int, end: Int, value: Boolean): Boolean {
        for(i in start..end) {
            if (bitSet[i] != value) return false
        }
        return true
    }

    fun isAllZero(): Boolean = synchronized(this) { isAll(writeableData, 0, getMaxBitSet() - 1, false) }

    fun isAllOne(): Boolean = synchronized(this) { isAll(writeableData, 0, getMaxBitSet() - 1, true) }

    fun flip(update: Boolean = false) {
        writeableData.flip(0, getMaxBitSet() - 1)
        if(update) update()
    }

    fun toByteArray(): ByteArray = writeableData.toByteArray()
}