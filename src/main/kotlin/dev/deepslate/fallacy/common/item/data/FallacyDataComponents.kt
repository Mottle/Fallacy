package dev.deepslate.fallacy.common.item.data

import com.mojang.serialization.Codec
import dev.deepslate.fallacy.Fallacy
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
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
        builder.persistent(CladdingData.CODEC).networkSynchronized(CladdingData.STREAM_CODEC)
    }

    val ROCK_SKIN_REGENERATION_REST_TICKS =
        REGISTER.registerComponentType("rock_skin_regeneration_rest_ticks") { builder ->
            builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT)
        }

    val FOOD_DIET = REGISTER.registerComponentType("food_diet") { builder ->
        builder.persistent(DietData.CODEC).networkSynchronized(DietData.STREAM_CODEC)
    }

    fun init(bus: IEventBus) {
        REGISTER.register(bus)
    }
}