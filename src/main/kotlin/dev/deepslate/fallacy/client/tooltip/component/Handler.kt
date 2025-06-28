package dev.deepslate.fallacy.client.tooltip.component

import dev.deepslate.fallacy.Fallacy
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent

@EventBusSubscriber(modid = Fallacy.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object Handler {
    @SubscribeEvent
    fun register(event: RegisterClientTooltipComponentFactoriesEvent) {
        event.register(ItemTooltipComponent::class.java, ::ItemTooltipRenderComponent)
    }
}