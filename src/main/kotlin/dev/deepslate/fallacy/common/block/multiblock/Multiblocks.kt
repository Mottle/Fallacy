package dev.deepslate.fallacy.common.block.multiblock

import dev.deepslate.fallacy.Fallacy
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegistryBuilder

object Multiblocks {
    val KEY: ResourceKey<Registry<Multiblock>> = ResourceKey.createRegistryKey(Fallacy.withID("multiblock"))

    val REGISTRY: Registry<Multiblock> = RegistryBuilder(KEY).sync(true).maxId(2048).create()

    private val registry = DeferredRegister.create(REGISTRY, Fallacy.MOD_ID)

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