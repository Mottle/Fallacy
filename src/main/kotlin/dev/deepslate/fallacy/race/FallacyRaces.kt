package dev.deepslate.fallacy.race

import dev.deepslate.fallacy.Fallacy
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegistryBuilder

@EventBusSubscriber(modid = Fallacy.MOD_ID)
object FallacyRaces {
    private val KEY = ResourceKey.createRegistryKey<Registry<Race>>(Fallacy.id("race"))

    private val REGISTRY = RegistryBuilder(KEY).sync(true).maxId(256).create()

    @SubscribeEvent
    fun registerRegistries(event: NewRegistryEvent) {
        event.register(REGISTRY)
    }

    val RACES = DeferredRegister.create(REGISTRY, Fallacy.MOD_ID)
}