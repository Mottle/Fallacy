package dev.deepslate.fallacy.client.hud

import dev.deepslate.fallacy.Fallacy
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.common.EventBusSubscriber.Bus.MOD
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent
import net.neoforged.neoforge.client.gui.VanillaGuiLayers

@EventBusSubscriber(modid = Fallacy.MOD_ID, value = [Dist.CLIENT], bus = MOD)
object ModEventHandler {
    @SubscribeEvent
    fun setLayers(event: RegisterGuiLayersEvent) {
        val rid = Fallacy.withID("layers")
        event.registerBelow(VanillaGuiLayers.SELECTED_ITEM_NAME, rid, LayerRender())
        Fallacy.LOGGER.info("Registering Vanilla gui layers.")
    }
}