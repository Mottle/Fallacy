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
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.level.LevelEvent
import net.neoforged.neoforge.network.PacketDistributor
import java.util.PriorityQueue

class ServerWeatherEngine(
    val level: ServerLevel,
    inputWeathers: List<WeatherInstance> = emptyList()
) : WeatherEngine {
    private val weatherPriorityQueue: PriorityQueue<WeatherInstance> =
        PriorityQueue(compareByDescending { it.priority })

    init {
        weatherPriorityQueue.addAll(inputWeathers)
    }

    val sortedWeathers: List<WeatherInstance>
        get() = weatherPriorityQueue.toList()

    val size: Int
        get() = weatherPriorityQueue.size

    var isDirty: Boolean = false

    fun markDirty() {
        isDirty = true
    }

    override fun tick() {
        weatherPriorityQueue.forEach { weather -> weather.tick(level) }
        clean()
        if (TickHelper.checkServerSecondRate(30)) schedule()

        //若天气发生变化则向客户端同步
        if (isDirty) {
            syncAll()
            isDirty = false
        }
    }

    private fun syncAll() {
        PacketDistributor.sendToAllPlayers(WeatherSyncPacket(sortedWeathers))
    }

    fun sync(player: ServerPlayer) {
        PacketDistributor.sendToPlayer(player, WeatherSyncPacket(sortedWeathers))
    }

    override fun getWeatherAt(pos: BlockPos): WeatherInstance {
        val weather = weatherPriorityQueue.find { w -> w.isIn(pos) && w.isValidIn(level, pos) && !w.isEnded }
        return weather ?: WeatherInstance(Clear, region = UniversalRegion)
    }

    override fun isWet(pos: BlockPos): Boolean = getWeatherAt(pos).isWet

    private fun clean() {
        val removed = weatherPriorityQueue.filter { it.isEnded }

        if (removed.isEmpty()) return

        removed.forEach {
            weatherPriorityQueue.remove(it)
        }
        isDirty = true
    }

    fun schedule() {
        val weather = WeatherInstance.create(FallacyWeathers.THUNDER, TickHelper.minute(10), UniversalRegion)
        weatherPriorityQueue.add(weather)
        isDirty = true
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
            val data = engine.sortedWeathers

            serverLevel.setData(FallacyAttachments.Level.WEATHERS, data)
        }

        @SubscribeEvent
        fun onPlayerJoin(event: PlayerEvent.PlayerLoggedInEvent) {
            val level = event.entity.level() as ServerLevel
            val engine = (level.weatherEngine as? ServerWeatherEngine) ?: return
            engine.sync(event.entity as ServerPlayer)
        }
    }
}