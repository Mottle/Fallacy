package dev.deepslate.fallacy.util

import dev.deepslate.fallacy.Fallacy
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ClientTickEvent

@EventBusSubscriber(modid = Fallacy.MOD_ID)
object TickHelper {
    private var clientTickCounter = 0

    @SubscribeEvent
    fun onClientTick(event: ClientTickEvent.Post) {
        clientTickCounter++
    }

    val currentClientTick
        get() = clientTickCounter

    val currentServerTick
        get() = ServerHelper.server?.tickCount ?: -1

    fun checkServerTickRate(rate: Int) = currentServerTick % rate == 0

    fun second(sec: Int) = sec * 20

    fun minute(min: Int) = second(60 * min)
}