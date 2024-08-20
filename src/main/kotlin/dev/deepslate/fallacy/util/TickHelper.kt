package dev.deepslate.fallacy.util

import dev.deepslate.fallacy.Fallacy
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ClientTickEvent

@EventBusSubscriber(modid = Fallacy.MOD_ID, value = [Dist.CLIENT])
object TickHelper {
    private var clientTickCounter = 0

    @SubscribeEvent
    fun onTick(event: ClientTickEvent.Post) {
        clientTickCounter++
    }

    val currentClientTick
        get() = clientTickCounter
}