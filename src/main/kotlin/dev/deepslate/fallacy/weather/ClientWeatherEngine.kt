package dev.deepslate.fallacy.weather

import dev.deepslate.fallacy.util.extension.internalWeatherEngine
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.world.level.Level
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.neoforge.event.level.LevelEvent

class ClientWeatherEngine : WeatherEngine {
    override fun tick() {
    }

    object Handler {
        @SubscribeEvent
        fun onLevelLoad(event: LevelEvent.Load) {
            if (!event.level.isClientSide) return
            if ((event.level as ClientLevel).dimension() != Level.OVERWORLD) return
            val level = event.level as ClientLevel
            level.internalWeatherEngine = ClientWeatherEngine()
        }
    }
}