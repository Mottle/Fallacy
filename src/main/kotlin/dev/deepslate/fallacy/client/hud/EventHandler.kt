package dev.deepslate.fallacy.client.hud

import dev.deepslate.fallacy.Fallacy
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent
import net.neoforged.neoforge.client.gui.VanillaGuiLayers

@EventBusSubscriber(modid = Fallacy.MOD_ID, value = [Dist.CLIENT])
object EventHandler {
    private val vanillaOverlays = listOf(
        VanillaGuiLayers.AIR_LEVEL, VanillaGuiLayers.ARMOR_LEVEL,
        VanillaGuiLayers.PLAYER_HEALTH, VanillaGuiLayers.VEHICLE_HEALTH, VanillaGuiLayers.FOOD_LEVEL
    )

    //移除原版bar
    @SubscribeEvent
    fun disableVanillaOverlay(event: RenderGuiLayerEvent.Pre) {
        val name = event.name
        if (vanillaOverlays.contains(name)) event.isCanceled = true
    }


}