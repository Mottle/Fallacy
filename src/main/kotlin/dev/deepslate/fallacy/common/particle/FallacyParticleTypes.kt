package dev.deepslate.fallacy.common.particle

import dev.deepslate.fallacy.Fallacy
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.core.registries.BuiltInRegistries
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object FallacyParticleTypes {
    private val registry = DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, Fallacy.MOD_ID)

    val CLOUD_256: DeferredHolder<ParticleType<*>, SimpleParticleType> = registry.register("cloud_256") { _ ->
        SimpleParticleType(false)
    }

    fun init(bus: IEventBus) {
        registry.register(bus)
    }
}