package dev.deepslate.fallacy.weather.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.network.packet.WeatherSyncPacket
import dev.deepslate.fallacy.util.extension.internalWeatherEngine
import dev.deepslate.fallacy.util.extension.weatherEngine
import dev.deepslate.fallacy.util.region.UniversalRegion
import dev.deepslate.fallacy.weather.WeatherEngine
import dev.deepslate.fallacy.weather.WeatherInstance
import dev.deepslate.fallacy.weather.WeatherStorage
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.level.LevelEvent
import net.neoforged.neoforge.network.handling.IPayloadContext
import java.util.*

class ClientWeatherEngine(val level: ClientLevel) : WeatherEngine, WeatherStorage {

    companion object {
        val weatherHere: WeatherInstance?
            get() {
                val player = Minecraft.getInstance().player ?: return null
                val level = player.level()
                val engine = level.weatherEngine ?: return null

                return engine.getWeatherAt(player.blockPosition())
            }
    }

    override val weatherStorage = PriorityQueue<WeatherInstance>(compareByDescending { w -> w.priority })

    fun setWeathers(weather: List<WeatherInstance>) {
        weatherStorage.clear()
        weatherStorage.addAll(weather)
    }

    override fun tick() {}

    @EventBusSubscriber(modid = Fallacy.Companion.MOD_ID)
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

            Fallacy.Companion.LOGGER.info("Syncing weather.")
        }
    }

    override fun getWeatherAt(pos: BlockPos): WeatherInstance {
        val weather = weatherStorage.find { w -> w.contains(pos) && w.isValidAt(level, pos) && !w.isEnded }
        return weather ?: WeatherInstance(Clear, region = UniversalRegion)
    }

    override fun isWet(pos: BlockPos): Boolean = getWeatherAt(pos).isWet
}