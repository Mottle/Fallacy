package dev.deepslate.fallacy.weather

import dev.deepslate.fallacy.util.TickHelper
import dev.deepslate.fallacy.util.extension.weatherEngine
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.tick.LevelTickEvent

interface WeatherEngine {
    fun tick()

    fun getWeatherAt(pos: BlockPos): WeatherInstance

    fun isWet(pos: BlockPos): Boolean

    object Handler {

        const val SEC = 20

        @SubscribeEvent
        fun onLevelTick(event: LevelTickEvent.Post) {
            val level = event.level as Level

            if (!TickHelper.checkServerTickRate(SEC)) return
            if (level.dimension() != Level.OVERWORLD) return

            level.weatherEngine?.tick()
        }
    }
}