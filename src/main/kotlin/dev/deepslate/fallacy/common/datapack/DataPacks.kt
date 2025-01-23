package dev.deepslate.fallacy.common.datapack

import dev.deepslate.fallacy.Fallacy
import net.minecraft.core.Registry
import net.minecraft.core.RegistrySetBuilder
import net.minecraft.resources.ResourceKey
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import net.neoforged.neoforge.data.event.GatherDataEvent
import net.neoforged.neoforge.registries.DataPackRegistryEvent

@EventBusSubscriber(modid = Fallacy.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object DataPacks {

    val CROP_REGISTRY_KEY: ResourceKey<Registry<CropsConfiguration>> =
        ResourceKey.createRegistryKey<CropsConfiguration>(Fallacy.id("crop"))

    @SubscribeEvent
    fun register(event: DataPackRegistryEvent.NewRegistry) {
        event.dataPackRegistry(CROP_REGISTRY_KEY, CropsConfiguration.CODEC)
    }

    @SubscribeEvent
    fun onGatherData(event: GatherDataEvent) {
        event.generator.addProvider<DatapackBuiltinEntriesProvider>(event.includeServer()) { output ->
            DatapackBuiltinEntriesProvider(
                output,
                event.lookupProvider,
                RegistrySetBuilder().add(CROP_REGISTRY_KEY) { bootstrap ->
                    bootstrap.register(
                        CropsConfiguration.CONFIG_KEY,
                        CropsConfiguration.generateDefaultDataPack()
                    )
                },
                setOf(Fallacy.MOD_ID)
            )
        }
    }
}