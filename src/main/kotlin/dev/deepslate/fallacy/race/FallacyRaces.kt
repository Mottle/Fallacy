package dev.deepslate.fallacy.race

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.race.impl.*
import net.minecraft.resources.ResourceKey
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegistryBuilder

@EventBusSubscriber(modid = Fallacy.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object FallacyRaces {
    val KEY = ResourceKey.createRegistryKey<Race>(Fallacy.id("race"))

    val REGISTRY = RegistryBuilder(KEY).sync(true).maxId(256).create()

    private val registry = DeferredRegister.create(REGISTRY, Fallacy.MOD_ID)

    @SubscribeEvent
    fun registerRegistries(event: NewRegistryEvent) {
        event.register(REGISTRY)
    }

    fun init(bus: IEventBus) {
        registry.register(bus)
    }

    val UNKNOWN = registry.register(Unknown.ID.path) { _ -> Unknown.INSTANCE }

    val HUMANKIND = registry.register(Humankind.ID.path) { _ -> Humankind() }

    val ZOMBIE = registry.register(Zombie.ID.path) { _ -> Zombie() }

    val GYNOU = registry.register(Gynou.ID.path) { _ -> Gynou() }

    val ROCK = registry.register(Rock.ID.path) { _ -> Rock() }

    val GOD = registry.register(God.ID.path) { _ -> God() }

    val WOOD = registry.register(Wood.ID.path) { _ -> Wood() }

    val ELF = registry.register(Elf.ID.path) { _ -> Elf() }

    val ORC = registry.register(Orc.ID.path) { _ -> Orc() }

    val SKELETON = registry.register(Skeleton.ID.path) { _ -> Skeleton() }
}