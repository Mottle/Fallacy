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

    val BIOME_REGISTRY_KEY: ResourceKey<Registry<BiomesConfiguration>> =
        ResourceKey.createRegistryKey<BiomesConfiguration>(Fallacy.id("biome"))

    @SubscribeEvent
    fun register(event: DataPackRegistryEvent.NewRegistry) {
        event.dataPackRegistry(CROP_REGISTRY_KEY, CropsConfiguration.CODEC)
        event.dataPackRegistry(BIOME_REGISTRY_KEY, BiomesConfiguration.CODEC)
    }

    @SubscribeEvent
    fun onGatherData(event: GatherDataEvent) {
        event.generator.addProvider<DatapackBuiltinEntriesProvider>(event.includeServer()) { output ->
            DatapackBuiltinEntriesProvider(
                output,
                event.lookupProvider,
                RegistrySetBuilder().add(CROP_REGISTRY_KEY) { bs ->
                    bs.register(CropsConfiguration.CONFIGURATION_KEY, CropsConfiguration.generateDefaultPack())
                }.add(BIOME_REGISTRY_KEY) { bs ->
                    bs.register(BiomesConfiguration.CONFIGURATION_KEY, BiomesConfiguration.generateDefaultPack())
                },
                setOf(Fallacy.MOD_ID)
            )
        }
    }
}