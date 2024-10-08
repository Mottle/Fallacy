package dev.deepslate.fallacy.common.biome.data

import dev.deepslate.fallacy.common.block.data.NPK

data class BiomeSetting(val npk: NPK, val temperature: UInt) {
    class Builder {
        var npk: NPK = NPK.zero()

        var temperature: UInt = 273u

        fun withNPK(npk: NPK) {
            this.npk = npk
        }

        fun withTemperature(temperature: UInt) {
            this.temperature = temperature
        }

        fun build(): BiomeSetting = BiomeSetting(npk, temperature)
    }

    companion object {
        fun default() = Builder().build()
    }
}
