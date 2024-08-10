package dev.deepslate.fallacy.util

import com.mojang.blaze3d.systems.RenderSystem
import kotlin.math.floor

data class RGB(val red: Int, val green: Int, val blue: Int) {
    companion object {
        fun from(red: Int, green: Int, blue: Int): RGB = RGB(red, green, blue)

        val BLACK = RGB(0, 0, 0)
        val RED = RGB(0xff, 0, 0)
        val YELLOW = RGB(0xff, 0xff, 0)

        fun reset() {
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f)
        }

        fun fromHex(hex: String): RGB {
            val value = Integer.decode(hex)
            val r = (value shr 16) and 0xff
            val g = (value shr 8) and 0xff
            val b = value and 0xff
            return RGB(r, g, b)
        }
    }

    fun blend(other: RGB, rate: Float): RGB {
        val r = floor(this.red * (1 - rate) + other.red * rate).toInt()
        val g = floor(this.green * (1 - rate) + other.green * rate).toInt()
        val b = floor(this.blue * (1 - rate) + other.blue * rate).toInt()
        return RGB.from(r, g, b);
    }

    val value: Int
        get() = (red shl 16) + (green shl 8) + blue

    fun color2GLa(alpha: Float) {
        val r = red / 255.0f
        val g = green / 255.0f
        val b = blue / 255.0f
        RenderSystem.setShaderColor(r, g, b, alpha)
    }

    fun color2GL() = color2GLa(1f)
}