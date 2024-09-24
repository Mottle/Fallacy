package dev.deepslate.fallacy.client.screen

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.client.screen.vanilla.VanillaDynamicButton
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.components.WidgetSprites
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ScreenEvent

@EventBusSubscriber(modid = Fallacy.MOD_ID, value = [Dist.CLIENT])
object Handler {
    @SubscribeEvent
    fun onGuiInit(event: ScreenEvent.Init.Post) {
        val screen = event.screen as? InventoryScreen ?: return
        val on = Fallacy.id("on")
        val off = Fallacy.id("off")
        val sprites = WidgetSprites(off, on)
        val button = VanillaDynamicButton(screen, screen.guiLeft + 126, screen.height / 2 - 22, 20, 18, sprites) { _ ->
//            Minecraft.getInstance().setScreen(HealthScreen(true))
        }
        event.addListener(button)
    }
}