package dev.deepslate.fallacy.util

import kotlin.math.exp

object MathFunctions {
    fun sigmoid(x: Double): Double {
        val nExp = exp(-x)
        return 1.0 / (1.0 + nExp)
    }

    fun nigmoid(x: Double): Double = 2 * (sigmoid(x) - 0.5)
}