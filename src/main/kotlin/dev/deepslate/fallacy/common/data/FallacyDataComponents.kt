package dev.deepslate.fallacy.common.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
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
        val pairCodec = RecordCodecBuilder.create { instance ->
            instance.group(
                ResourceLocation.CODEC.fieldOf("id").forGetter(
                    Pair<ResourceLocation, Int>::first
                ), Codec.INT.fieldOf("duration").forGetter(Pair<ResourceLocation, Int>::second)
            ).apply(instance, ::Pair)
        }
        val codec = Codec.list(pairCodec)
        val pairStreamCodec = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC, Pair<ResourceLocation, Int>::first,
            ByteBufCodecs.INT, Pair<ResourceLocation, Int>::second, ::Pair
        )
        val streamCodec = pairStreamCodec.apply(ByteBufCodecs.list(16))
        builder.persistent(codec).networkSynchronized(streamCodec)
    }

    val ROCK_SKIN_REGENERATION_REST_TICKS =
        REGISTER.registerComponentType("rock_skin_regeneration_rest_ticks") { builder ->
            builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT)
        }

    fun init(bus: IEventBus) {
        REGISTER.register(bus)
    }
}