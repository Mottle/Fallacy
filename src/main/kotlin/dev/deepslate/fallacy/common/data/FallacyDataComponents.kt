package dev.deepslate.fallacy.common.data

import com.mojang.serialization.Codec
import dev.deepslate.fallacy.Fallacy
import net.minecraft.network.codec.ByteBufCodecs
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister

object FallacyDataComponents {
    private val REGISTER = DeferredRegister.createDataComponents(Fallacy.MOD_ID)

    val HYDRATION = REGISTER.registerComponentType("hydration") { builder ->
        builder.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT)
    }

    val IS_GYNOU_WINGS = REGISTER.registerComponentType("is_gynou_wings") { builder ->
        builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
    }

    val FORCE_BINDING = REGISTER.registerComponentType("force_binding") { builder ->
        builder.persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL)
    }

    fun init(bus: IEventBus) {
        REGISTER.register(bus)
    }
}