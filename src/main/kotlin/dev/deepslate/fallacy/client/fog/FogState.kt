package dev.deepslate.fallacy.client.fog

import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

@OnlyIn(Dist.CLIENT)
data class FogState(
    var rgb: Triple<Float, Float, Float>,
    var fogStart: Float, var fogEnd: Float,
    var skyStart: Float = fogStart, var skyEnd: Float = fogEnd
) {
    companion object {
        fun of(state: FogState) = state.copy()

        fun zero() = FogState(Triple(0f, 0f, 0f), 0f, 0f)
    }
}