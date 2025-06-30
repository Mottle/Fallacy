package dev.deepslate.fallacy.common.data

import com.mojang.serialization.Codec
import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.trait.BehaviorContainer
import dev.deepslate.fallacy.common.data.player.FoodHistory
import dev.deepslate.fallacy.common.data.player.NutritionState
import dev.deepslate.fallacy.race.impl.Unknown
import dev.deepslate.fallacy.thermodynamics.HeatProcessState
import dev.deepslate.fallacy.thermodynamics.data.HeatStorage
import dev.deepslate.fallacy.weather.WeatherInstance
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries

object FallacyAttachments {
    private val registry = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Fallacy.MOD_ID)

    internal val POSITIVE_CHUNK_HEAT: DeferredHolder<AttachmentType<*>, AttachmentType<HeatStorage>> =
        registry.register("positive_chunk_heat") { _ ->
            AttachmentType.builder(HeatStorage::of).serialize(HeatStorage.CODEC).build()
        }

    internal val NEGATIVE_CHUNK_HEAT: DeferredHolder<AttachmentType<*>, AttachmentType<HeatStorage>> =
        registry.register("negative_chunk_heat") { _ ->
            AttachmentType.builder(HeatStorage::of).serialize(HeatStorage.CODEC).build()
        }

    internal val HEAT_PROCESS_STATE: DeferredHolder<AttachmentType<*>, AttachmentType<HeatProcessState>> =
        registry.register("heat_process_state") { _ ->
            AttachmentType.builder { _ -> HeatProcessState.UNPROCESSED }.serialize(HeatProcessState.CODEC).build()
        }

    val THIRST: DeferredHolder<AttachmentType<*>, AttachmentType<Float>> = registry.register("thirst") { _ ->
        AttachmentType.builder { _ -> 20f }.serialize(Codec.FLOAT).build()
    }

    //客户端和服务端各自维护一个LAST_DRINK_TICK，不进行同步
    internal val LAST_DRINK_TICK_STAMP = registry.register("last_drink_tick_stamp") { _ ->
        AttachmentType.builder { _ -> -1 }.build()
    }

    val RACE_ID: DeferredHolder<AttachmentType<*>, AttachmentType<ResourceLocation>> =
        registry.register("race_id") { _ ->
            AttachmentType.builder { _ -> Unknown.ID }.serialize(ResourceLocation.CODEC).copyOnDeath().build()
        }

    val BEHAVIORS: DeferredHolder<AttachmentType<*>, AttachmentType<BehaviorContainer>> =
        registry.register("behaviors") { _ ->
            AttachmentType.builder(BehaviorContainer::empty).serialize(BehaviorContainer.CODEC).copyOnDeath().build()
        }

    val NUTRITION_STATE: DeferredHolder<AttachmentType<*>, AttachmentType<NutritionState>> =
        registry.register("nutrition_state") { _ ->
            AttachmentType.builder { _ -> NutritionState() }.serialize(NutritionState.CODEC).copyOnDeath().build()
        }

    val FOOD_HISTORY: DeferredHolder<AttachmentType<*>, AttachmentType<FoodHistory>> =
        registry.register("food_history") { _ ->
            AttachmentType.builder { _ -> FoodHistory() }.serialize(FoodHistory.CODEC).copyOnDeath().build()
        }

    val BONE: DeferredHolder<AttachmentType<*>, AttachmentType<Float>> = registry.register("bone") { _ ->
        AttachmentType.builder { _ -> 10f }.serialize(Codec.FLOAT).build()
    }

    val BODY_HEAT: DeferredHolder<AttachmentType<*>, AttachmentType<Float>> = registry.register("body_heat") { _ ->
        AttachmentType.builder { p ->
            if (p is LivingEntity) {
                return@builder p.getAttribute(FallacyAttributes.DEFAULT_BODY_HEAT)?.value?.toFloat() ?: 0f
            }
            return@builder 0f
        }.serialize(Codec.FLOAT).build()
    }

    object Level {
        val WEATHERS: DeferredHolder<AttachmentType<*>, AttachmentType<List<WeatherInstance>>> =
            registry.register("weathers") { _ ->
                AttachmentType.builder { _ -> emptyList<WeatherInstance>() }
                    .serialize(Codec.list(WeatherInstance.CODEC))
                    .build()
            }
    }

    fun register(bus: IEventBus) {
        registry.register(bus)
    }
}