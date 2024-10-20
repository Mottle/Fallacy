package dev.deepslate.fallacy.weather

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.util.extension.internalWeatherEngine
import dev.deepslate.fallacy.util.region.UniversalRegion
import dev.deepslate.fallacy.weather.impl.Clear
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.level.LevelEvent
import java.util.PriorityQueue

class ClientWeatherEngine(val level: ClientLevel) : WeatherEngine {
    private val weatherQueue = PriorityQueue<WeatherInstance>(compareByDescending { w -> w.priority })

    fun setWeathers(weather: List<WeatherInstance>) {
        weatherQueue.clear()
        weatherQueue.addAll(weather)
    }

    override fun tick() {}

    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object Handler {
        @SubscribeEvent
        fun onLevelLoad(event: LevelEvent.Load) {
            if (!event.level.isClientSide) return
            if ((event.level as ClientLevel).dimension() != Level.OVERWORLD) return
            val level = event.level as ClientLevel
            level.internalWeatherEngine = ClientWeatherEngine(level)
        }
    }

    override fun getWeatherAt(pos: BlockPos): WeatherInstance {
        val weather = weatherQueue.find { w -> w.isIn(pos) && w.isValidIn(level, pos) }
        return weather ?: WeatherInstance(Clear, region = UniversalRegion)
    }

    override fun isWet(pos: BlockPos): Boolean = getWeatherAt(pos).isWet
}