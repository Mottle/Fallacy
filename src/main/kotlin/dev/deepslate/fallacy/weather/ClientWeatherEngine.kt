package dev.deepslate.fallacy.weather

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.network.packet.WeatherSyncPacket
import dev.deepslate.fallacy.util.extension.internalWeatherEngine
import dev.deepslate.fallacy.util.extension.weatherEngine
import dev.deepslate.fallacy.util.region.UniversalRegion
import dev.deepslate.fallacy.weather.impl.Clear
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.level.LevelEvent
import net.neoforged.neoforge.network.handling.IPayloadContext
import java.util.PriorityQueue

class ClientWeatherEngine(val level: ClientLevel) : WeatherEngine, WeatherStorage {
    override val weatherStorage = PriorityQueue<WeatherInstance>(compareByDescending { w -> w.priority })

    fun setWeathers(weather: List<WeatherInstance>) {
        weatherStorage.clear()
        weatherStorage.addAll(weather)
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

        fun handleWeatherSync(data: WeatherSyncPacket, context: IPayloadContext) {
            val engine = context.player().level().weatherEngine ?: return
            val clientEngine = engine as ClientWeatherEngine

            clientEngine.setWeathers(data.weathers)

            Fallacy.LOGGER.info("Syncing weather.")
        }
    }

    override fun getWeatherAt(pos: BlockPos): WeatherInstance {
        val weather = weatherStorage.find { w -> w.isIn(pos) && w.isValidIn(level, pos) && !w.isEnded }
        return weather ?: WeatherInstance(Clear, region = UniversalRegion)
    }

    override fun isWet(pos: BlockPos): Boolean = getWeatherAt(pos).isWet
}