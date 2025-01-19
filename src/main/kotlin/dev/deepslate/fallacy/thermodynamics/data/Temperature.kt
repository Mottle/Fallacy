package dev.deepslate.fallacy.thermodynamics.data

import dev.deepslate.fallacy.thermodynamics.ThermodynamicsEngine

sealed interface Temperature : Comparable<Temperature> {
    val heat: Int

    override fun compareTo(other: Temperature): Int = heat.compareTo(other.heat)

    data class Kelvins(val value: Int) : Temperature {
        override val heat: Int get() = value.coerceIn(MIN_VALUE, MAX_VALUE)

        companion object {
            const val MIN_VALUE = ThermodynamicsEngine.MIN_HEAT

            const val MAX_VALUE = ThermodynamicsEngine.MAX_HEAT
        }

        fun toCelsius(): Celsius = Celsius(value - ThermodynamicsEngine.FREEZING_POINT)

        override fun toString(): String = "${value}K"
    }

    data class Celsius(val value: Int) : Temperature {
        override val heat: Int get() = value.coerceIn(MIN_VALUE, MAX_VALUE) + ThermodynamicsEngine.FREEZING_POINT

        companion object {
            const val MIN_VALUE = -ThermodynamicsEngine.FREEZING_POINT
            const val MAX_VALUE = ThermodynamicsEngine.MAX_HEAT - ThermodynamicsEngine.FREEZING_POINT
        }

        fun toKelvins(): Kelvins = Kelvins(heat)

        override fun toString(): String = "${value}°C"
    }

    companion object {
        fun celsius(heat: Int): Celsius = Celsius(heat - ThermodynamicsEngine.FREEZING_POINT)

        fun kelvins(heat: Int): Kelvins = Kelvins(heat)
    }
}