package dev.deepslate.fallacy.weather

import net.minecraft.world.level.Level
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.tick.LevelTickEvent
import java.util.UUID

interface WeatherEngine {
    fun tick()

    companion object {
        private var weatherEngineMap = mutableMapOf<UUID, WeatherEngine>()
    }

    object Handler {

        @SubscribeEvent
        fun onLevelTick(event: LevelTickEvent.Post) {
            val level = event.level as Level
        }
    }
}