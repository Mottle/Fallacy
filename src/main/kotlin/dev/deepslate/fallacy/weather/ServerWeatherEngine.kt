package dev.deepslate.fallacy.weather

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.util.extension.internalWeatherEngine
import dev.deepslate.fallacy.util.extension.weatherEngine
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.level.LevelEvent

class ServerWeatherEngine(
    val level: ServerLevel,
    private var weatherList: MutableList<WeatherInstance> = mutableListOf<WeatherInstance>()
) : WeatherEngine {

    val weathers: List<WeatherInstance>
        get() = weatherList.toList()

    override fun tick() {
    }

    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object Handler {
        @SubscribeEvent
        fun onServerLevelLoad(event: LevelEvent.Load) {
            val level = event.level as Level

            if (level.isClientSide) return
            if (level.dimension() != Level.OVERWORLD) return

            val serverLevel = level as ServerLevel
            val weathers = serverLevel.getData(FallacyAttachments.Level.WEATHERS)

            level.internalWeatherEngine = ServerWeatherEngine(serverLevel, weathers.toMutableList())
        }

        @SubscribeEvent
        fun onServerLevelSave(event: LevelEvent.Save) {
            val level = event.level as Level

            if (level.isClientSide) return
            if (level.dimension() != Level.OVERWORLD) return

            val serverLevel = event.level as ServerLevel
            val engine = (serverLevel.weatherEngine as? ServerWeatherEngine) ?: return
            val data = engine.weathers

            serverLevel.setData(FallacyAttachments.Level.WEATHERS, data)
        }
    }
}