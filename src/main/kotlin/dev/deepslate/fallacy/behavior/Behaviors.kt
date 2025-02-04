package dev.deepslate.fallacy.behavior

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.behavior.impl.*
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegistryBuilder

object Behaviors {
    val KEY: ResourceKey<Registry<Behavior>> = ResourceKey.createRegistryKey<Behavior>(Fallacy.id("behavior"))

    val REGISTRY: Registry<Behavior> = RegistryBuilder(KEY).sync(false).create()

    private val registry = DeferredRegister.create(REGISTRY, Fallacy.MOD_ID)

    val UNDEAD: DeferredHolder<Behavior, SymbolBehavior> = registry.register("undead", SymbolBehavior::of)

    val BURNING_IN_SUNLIGHT: DeferredHolder<Behavior, BurningInSunlight> =
        registry.register("burning_in_sunlight", ::BurningInSunlight)

    val WEAKNESS_IN_SUNLIGHT: DeferredHolder<Behavior, WeaknessInSunlight> =
        registry.register("weakness_in_sunlight", ::WeaknessInSunlight)

    val WEAKNESS_2_IN_SUNLIGHT: DeferredHolder<Behavior, Weakness2InSunlight> =
        registry.register("weakness_2_in_sunlight", ::Weakness2InSunlight)

    val WEAKNESS_IN_DAY: DeferredHolder<Behavior, WeaknessInDay> = registry.register("weakness_in_day", ::WeaknessInDay)

    @EventBusSubscriber(modid = Fallacy.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    object Handler {
        @SubscribeEvent
        fun registerRegistries(event: NewRegistryEvent) {
            event.register(REGISTRY)
        }
    }

    fun init(bus: IEventBus) {
        registry.register(bus)
    }
}