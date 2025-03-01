package dev.deepslate.fallacy.common.item.component

import com.mojang.serialization.Codec
import dev.deepslate.fallacy.Fallacy
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.Unit
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object FallacyDataComponents {
    private val registry = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Fallacy.MOD_ID)

    val OUTDATED: DeferredHolder<DataComponentType<*>, DataComponentType<Unit>> =
        registry.registerComponentType("outdated") { builder ->
            builder.persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE))
        }

    val DEPRECATED: DeferredHolder<DataComponentType<*>, DataComponentType<Unit>> =
        registry.registerComponentType("deprecated") { builder ->
            builder.persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE))
        }

    val HYDRATION: DeferredHolder<DataComponentType<*>, DataComponentType<Float>> =
        registry.registerComponentType("hydration") { builder ->
            builder.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT)
        }

    val GYNOU_WINGS: DeferredHolder<DataComponentType<*>, DataComponentType<Unit>> =
        registry.registerComponentType("is_gynou_wings") { builder ->
            builder.persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE))
        }

    val FORCE_BINDING: DeferredHolder<DataComponentType<*>, DataComponentType<Unit>> =
        registry.registerComponentType("force_binding") { builder ->
            builder.persistent(Unit.CODEC).networkSynchronized(StreamCodec.unit(Unit.INSTANCE))
        }

    val CLADDINGS: DeferredHolder<DataComponentType<*>, DataComponentType<CladdingData>> =
        registry.registerComponentType("claddings") { builder ->
            builder.persistent(CladdingData.CODEC).networkSynchronized(CladdingData.STREAM_CODEC)
        }

    val ROCK_SKIN_REGENERATION_REST_TICKS: DeferredHolder<DataComponentType<*>, DataComponentType<Int>> =
        registry.registerComponentType("rock_skin_regeneration_rest_ticks") { builder ->
            builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT)
        }

    val RANK: DeferredHolder<DataComponentType<*>, DataComponentType<Int>> =
        registry.registerComponentType("rank") { builder ->
            builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT)
        }

    //食物营养度
    val NUTRITION: DeferredHolder<DataComponentType<*>, DataComponentType<NutritionData>> =
        registry.registerComponentType("nutrition") { builder ->
            builder.persistent(NutritionData.CODEC).networkSynchronized(NutritionData.STREAM_CODEC)
        }

    //食物饱腹等级 默认2
    val FULL_LEVEL: DeferredHolder<DataComponentType<*>, DataComponentType<Int>> =
        registry.registerComponentType("full_level") { builder ->
            builder.persistent(Codec.intRange(0, 4)).networkSynchronized(ByteBufCodecs.INT)
        }

    fun init(bus: IEventBus) {
        registry.register(bus)
    }
}