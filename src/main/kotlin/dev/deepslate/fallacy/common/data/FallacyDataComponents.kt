package dev.deepslate.fallacy.common.data

import com.mojang.serialization.Codec
import dev.deepslate.fallacy.Fallacy
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Unit
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister

object FallacyDataComponents {
    private val REGISTER = DeferredRegister.createDataComponents(Fallacy.MOD_ID)

    val HYDRATION = REGISTER.registerComponentType("hydration") { builder ->
        builder.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT)
    }

    val GYNOU_WINGS = REGISTER.registerComponentType("is_gynou_wings") { builder ->
        builder.persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE))
    }

    val FORCE_BINDING = REGISTER.registerComponentType("force_binding") { builder ->
        builder.persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE))
    }

    val CLADDINGS = REGISTER.registerComponentType("claddings") { builder ->
        val streamCodec = ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list(16))
        builder.persistent(ResourceLocation.CODEC.listOf()).networkSynchronized(streamCodec)
    }

    fun init(bus: IEventBus) {
        REGISTER.register(bus)
    }
}