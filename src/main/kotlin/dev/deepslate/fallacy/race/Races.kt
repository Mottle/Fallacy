package dev.deepslate.fallacy.race

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.race.impl.*
import dev.deepslate.fallacy.race.impl.rock.Rock
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
object Races {
    val KEY: ResourceKey<Registry<Race>> = ResourceKey.createRegistryKey<Race>(Fallacy.id("race"))

    val REGISTRY: Registry<Race> = RegistryBuilder(KEY).sync(true).maxId(256).create()

    private val registry = DeferredRegister.create(REGISTRY, Fallacy.MOD_ID)

    @SubscribeEvent
    fun registerRegistries(event: NewRegistryEvent) {
        event.register(REGISTRY)
    }

    fun init(bus: IEventBus) {
        registry.register(bus)
    }

    val UNKNOWN: DeferredHolder<Race, Unknown> = registry.register(Unknown.ID.path) { _ -> Unknown.INSTANCE }

    val HUMANKIND: DeferredHolder<Race, Humankind> = registry.register(Humankind.ID.path, ::Humankind)

    val ZOMBIE: DeferredHolder<Race, Zombie> = registry.register(Zombie.ID.path, ::Zombie)

    val GYNOU: DeferredHolder<Race, Gynou> = registry.register(Gynou.ID.path, ::Gynou)

    val ROCK: DeferredHolder<Race, Rock> = registry.register(Rock.ID.path, ::Rock)

    val GOD: DeferredHolder<Race, God> = registry.register(God.ID.path, ::God)

    val WOOD: DeferredHolder<Race, Wood> = registry.register(Wood.ID.path, ::Wood)

    val ELF: DeferredHolder<Race, Elf> = registry.register(Elf.ID.path, ::Elf)

    val ORC: DeferredHolder<Race, Orc> = registry.register(Orc.ID.path, ::Orc)

    val SKELETON: DeferredHolder<Race, Skeleton> = registry.register(Skeleton.ID.path, ::Skeleton)
}