package dev.deepslate.fallacy.client

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.network.packet.CladdingPacket
import dev.deepslate.fallacy.race.impl.rock.Helper
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.minecraft.world.inventory.ArmorSlot
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.ScreenEvent
import net.neoforged.neoforge.network.PacketDistributor

@EventBusSubscriber(modid = Fallacy.Companion.MOD_ID, value = [Dist.CLIENT])
class RockClientHandler {
    @SubscribeEvent
    fun onCladding(event: ScreenEvent.MouseButtonPressed.Post) {
        val screen = event.screen

        if (screen !is InventoryScreen && screen !is CreativeModeInventoryScreen) return

        val slot = screen.slotUnderMouse ?: return

        if (screen is InventoryScreen && slot !is ArmorSlot) return

        val armor = slot.item
        val carried = screen.menu.carried

        if (!Helper.checkCladding(armor, carried)) return

        PacketDistributor.sendToServer(CladdingPacket(slot.slotIndex))
    }
}