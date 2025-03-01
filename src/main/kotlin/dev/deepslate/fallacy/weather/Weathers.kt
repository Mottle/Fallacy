package dev.deepslate.fallacy.weather

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.weather.impl.*
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegistryBuilder

@EventBusSubscriber(modid = Fallacy.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object Weathers {
    val KEY: ResourceKey<Registry<Weather>> = ResourceKey.createRegistryKey<Weather>(Fallacy.withID("weather"))

    val REGISTRY: Registry<Weather> = RegistryBuilder(KEY).sync(true).maxId(256).create()

    @SubscribeEvent
    fun registerRegistries(event: NewRegistryEvent) {
        event.register(REGISTRY)
    }

    private val registry = DeferredRegister.create(REGISTRY, Fallacy.MOD_ID)

    val CLEAR: DeferredHolder<Weather, Clear> = registry.register(Clear.namespaceId.path) { _ -> Clear }

    val RAIN: DeferredHolder<Weather, Rain> = registry.register(Rain.ID.path) { _ -> Rain() }

    val THUNDER: DeferredHolder<Weather, Thunder> = registry.register(Thunder.ID.path) { _ -> Thunder() }

    val SANDSTORM: DeferredHolder<Weather, Sandstorm> = registry.register(Sandstorm.ID.path) { _ -> Sandstorm() }

    val SNOWSTORM: DeferredHolder<Weather, Snowstorm> = registry.register(Snowstorm.ID.path) { _ -> Snowstorm() }

    fun init(bus: IEventBus) {
        registry.register(bus)
    }
}