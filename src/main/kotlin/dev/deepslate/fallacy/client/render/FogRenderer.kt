package dev.deepslate.fallacy.client.render

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.client.fog.FogAdjuster
import dev.deepslate.fallacy.weather.impl.ClientWeatherEngine
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ClientTickEvent
import net.neoforged.neoforge.client.event.ViewportEvent

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = Fallacy.MOD_ID, value = [Dist.CLIENT])
object FogRenderer {

    private val fogAdjuster = FogAdjuster()

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onFogColors(event: ViewportEvent.ComputeFogColor) {
        fogAdjuster.onFogColor(event)
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onFogRender(event: ViewportEvent.RenderFog) {
        fogAdjuster.onFogRender(event)
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent.Pre) {
        val weather = ClientWeatherEngine.weatherHere?.weather ?: return
        fogAdjuster.tick(weather)
    }
}