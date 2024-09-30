package dev.deepslate.fallacy.common.item.component

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

    //食物营养度
    val NUTRITION = REGISTER.registerComponentType("nutrition") { builder ->
        builder.persistent(NutritionData.CODEC).networkSynchronized(NutritionData.STREAM_CODEC)
    }

    //食物饱腹等级 默认2
    val FULL_LEVEL = REGISTER.registerComponentType("full_level") { builder ->
        builder.persistent(Codec.intRange(0, 4)).networkSynchronized(ByteBufCodecs.INT)
    }

    fun init(bus: IEventBus) {
        REGISTER.register(bus)
    }
}