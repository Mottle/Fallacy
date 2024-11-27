package dev.deepslate.fallacy.thermodynamics.data

sealed interface Temperature : Comparable<Temperature> {
    val heat: UInt

    override fun compareTo(other: Temperature): Int = heat.compareTo(other.heat)

    data class Kelvins(val value: UInt) : Temperature {
        override val heat: UInt get() = value.coerceIn(MIN_VALUE, MAX_VALUE)

        companion object {
            const val MIN_VALUE: UInt = 0U
            const val MAX_VALUE: UInt = HeatLayer.MAX_HEAT
        }

        fun toCelsius(): Celsius = Celsius((value - HeatLayer.FREEZING_POINT).toInt())
    }

    data class Celsius(val value: Int) : Temperature {
        override val heat: UInt get() = (value.coerceIn(MIN_VALUE, MAX_VALUE) + 273).toUInt()

        companion object {
            const val MIN_VALUE: Int = -273
            const val MAX_VALUE: Int = 0xffff + MIN_VALUE
        }

        fun toKelvins(): Kelvins = Kelvins(heat)
    }
}