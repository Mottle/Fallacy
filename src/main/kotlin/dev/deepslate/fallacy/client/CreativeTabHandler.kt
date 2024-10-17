package dev.deepslate.fallacy.client

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.util.extension.extendedProperties
import net.minecraft.world.item.CreativeModeTab
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent

@EventBusSubscriber(modid = Fallacy.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object CreativeTabHandler {
    @SubscribeEvent
    fun onRegisterCreativeTabs(event: BuildCreativeModeTabContentsEvent) {
        event.parentEntries.filter { it.item.extendedProperties?.isDeprecated == true }.forEach {
            event.remove(it, CreativeModeTab.TabVisibility.PARENT_TAB_ONLY)
        }

        event.searchEntries.filter { it.item.extendedProperties?.isDeprecated == true }.forEach {
            event.remove(it, CreativeModeTab.TabVisibility.SEARCH_TAB_ONLY)
        }
    }
}