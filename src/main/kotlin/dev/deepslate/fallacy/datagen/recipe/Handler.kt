package dev.deepslate.fallacy.datagen.recipe

import dev.deepslate.fallacy.Fallacy
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.data.event.GatherDataEvent

@EventBusSubscriber(modid = Fallacy.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object Handler {
    @SubscribeEvent
    fun onGatherData(event: GatherDataEvent) {
        val output = event.generator.packOutput
        val lookupProvider = event.lookupProvider

        with(event.generator) {
            addProvider(
                event.includeServer(),
                FlintRecipeProvider(output, lookupProvider)
            )
        }
    }
}