package dev.deepslate.fallacy.weather

import net.minecraft.world.level.Level

interface WeatherEngine {
    val level: Level

    fun tick()
}