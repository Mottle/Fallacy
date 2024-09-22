package dev.deepslate.fallacy.race

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.race.impl.God
import dev.deepslate.fallacy.race.impl.Gynou
import dev.deepslate.fallacy.race.impl.Humankind
import dev.deepslate.fallacy.race.impl.Rock
import dev.deepslate.fallacy.race.impl.Unknown
import dev.deepslate.fallacy.race.impl.Wood
import dev.deepslate.fallacy.race.impl.Zombie
import net.minecraft.resources.ResourceKey
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegistryBuilder

@EventBusSubscriber(modid = Fallacy.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object FallacyRaces {
    private val KEY = ResourceKey.createRegistryKey<Race>(Fallacy.id("race"))

    val REGISTRY = RegistryBuilder(KEY).sync(true).maxId(256).create()

    val RACE = DeferredRegister.create(REGISTRY, Fallacy.MOD_ID)

    @SubscribeEvent
    fun registerRegistries(event: NewRegistryEvent) {
        event.register(REGISTRY)
    }

    fun init(bus: IEventBus) {
        RACE.register(bus)
    }

    val UNKNOWN = RACE.register(Unknown.ID.path) { _ -> Unknown.INSTANCE }

    val HUMANKIND = RACE.register(Humankind.ID.path) { _ -> Humankind() }

    val ZOMBIE = RACE.register(Zombie.ID.path) { _ -> Zombie() }

    val GYNOU = RACE.register(Gynou.ID.path) { _ -> Gynou() }

    val ROCK = RACE.register(Rock.ID.path) { _ -> Rock() }

    val GOD = RACE.register(God.ID.path) { _ -> God() }

    val WOOD = RACE.register(Wood.ID.path) { _ -> Wood() }
}