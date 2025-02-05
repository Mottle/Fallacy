package dev.deepslate.fallacy.weather

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.util.TickHelper
import dev.deepslate.fallacy.util.extension.weatherEngine
import dev.deepslate.fallacy.util.extension.windEngine
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.tick.LevelTickEvent

interface WeatherEngine {
    fun tick()

    fun getWeatherAt(pos: BlockPos): WeatherInstance

    fun isWet(pos: BlockPos): Boolean

    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object Handler {

        const val WEATHER_TICK_INTERVAL = 20

        const val WIND_TICK_INTERVAL = 5

        @SubscribeEvent
        fun onLevelTick(event: LevelTickEvent.Post) {
            val level = event.level as Level

            with(level) {
                if (TickHelper.checkServerTickRate(WEATHER_TICK_INTERVAL)) weatherEngine?.tick()
                if (TickHelper.checkServerTickRate(WIND_TICK_INTERVAL)) windEngine?.tick()
            }
        }
    }
}