package dev.deepslate.fallacy.util

import dev.deepslate.fallacy.Fallacy
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.server.ServerLifecycleHooks

object TickHelper {
    private var clientTickCounter = 0

    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object Handler {
        @SubscribeEvent
        fun onClientTick(event: ClientTickEvent.Post) {
            clientTickCounter++
        }
    }

    val currentClientTick
        get() = clientTickCounter

    val currentServerTick
        get() = ServerLifecycleHooks.getCurrentServer()?.tickCount ?: -1

    fun checkServerTickRate(rate: Int): Boolean = currentServerTick % rate == 0

    fun checkServerSecondRate(second: Int): Boolean = checkServerTickRate(second * 20)

    fun checkServerMinuteRate(minute: Int): Boolean = checkServerSecondRate(minute * 60)

    fun second(sec: Int) = sec * 20

    fun minute(min: Int) = second(60 * min)

    fun hour(hour: Int) = minute(60 * hour)
}