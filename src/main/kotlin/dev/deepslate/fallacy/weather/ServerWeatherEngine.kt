package dev.deepslate.fallacy.weather

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.common.network.packet.WeatherSyncPacket
import dev.deepslate.fallacy.util.TickHelper
import dev.deepslate.fallacy.util.extension.internalWeatherEngine
import dev.deepslate.fallacy.util.extension.weatherEngine
import dev.deepslate.fallacy.util.region.UniversalRegion
import dev.deepslate.fallacy.weather.impl.Clear
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.level.LevelEvent
import net.neoforged.neoforge.network.PacketDistributor
import java.util.PriorityQueue

class ServerWeatherEngine(
    val level: ServerLevel,
    inputWeathers: List<WeatherInstance> = emptyList()
) : WeatherEngine {
    val weatherPriorityQueue: PriorityQueue<WeatherInstance> = PriorityQueue(compareByDescending { it.priority })

    init {
        weatherPriorityQueue.addAll(inputWeathers)
    }

    val weathers: List<WeatherInstance>
        get() = weatherPriorityQueue.toList()

    val size: Int
        get() = weatherPriorityQueue.size

    override fun tick() {
        weatherPriorityQueue.forEach { weather -> weather.tick(level) }
        val hasCleaned = clean()
        val hasScheduled = if (TickHelper.checkServerSecondRate(30)) schedule() else false

        //若天气发生变化则向客户端同步
        if (hasCleaned || hasScheduled) {
            sync()
        }
    }

    private fun sync() {
        PacketDistributor.sendToAllPlayers(WeatherSyncPacket(weathers))
    }

    override fun getWeatherAt(pos: BlockPos): WeatherInstance {
        val weather = weatherPriorityQueue.find { w -> w.isIn(pos) && w.isValidIn(level, pos) }
        return weather ?: WeatherInstance(Clear, region = UniversalRegion)
    }

    override fun isWet(pos: BlockPos): Boolean = getWeatherAt(pos).isWet

    private fun clean(): Boolean {
        val removed = weatherPriorityQueue.filter { it.isEnded }

        if (removed.isEmpty()) return false

        removed.forEach {
            weatherPriorityQueue.remove(it)
        }
        return true
    }

    fun schedule(): Boolean {
        val weather = WeatherInstance.create(FallacyWeathers.RAIN, TickHelper.minute(10), UniversalRegion)
        weatherPriorityQueue.add(weather)
        return true
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