package dev.deepslate.fallacy.weather

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.weather.impl.Clear
import dev.deepslate.fallacy.weather.impl.Rain
import dev.deepslate.fallacy.weather.impl.Thunder
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegistryBuilder

@EventBusSubscriber(modid = Fallacy.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object FallacyWeathers {
    val KEY: ResourceKey<Registry<Weather>> = ResourceKey.createRegistryKey<Weather>(Fallacy.id("weather"))

    val REGISTRY = RegistryBuilder(KEY).sync(true).maxId(256).create()

    @SubscribeEvent
    fun registerRegistries(event: NewRegistryEvent) {
        event.register(REGISTRY)
    }

    private val registry = DeferredRegister.create(REGISTRY, Fallacy.MOD_ID)

    val CLEAR = registry.register(Clear.namespaceId.path) { _ -> Clear }

    val RAIN = registry.register(Rain.ID.path) { _ -> Rain() }

    val THUNDER = registry.register(Thunder.ID.path) { _ -> Thunder() }

    fun init(bus: IEventBus) {
        registry.register(bus)
    }
}