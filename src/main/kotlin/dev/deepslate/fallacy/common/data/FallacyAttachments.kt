package dev.deepslate.fallacy.common.data

import com.mojang.serialization.Codec
import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.player.NutritionState
import dev.deepslate.fallacy.race.impl.Unknown
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries

object FallacyAttachments {
    private val REGISTRY = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Fallacy.MOD_ID)

    val THIRST = REGISTRY.register("thirst") { _ ->
        AttachmentType.builder { _ -> 20f }.serialize(Codec.FLOAT).build()
    }

    //客户端和服务端各自维护一个LAST_DRINK_TICK，不进行同步
    internal val LAST_DRINK_TICK = REGISTRY.register("last_drink_tick") { _ ->
        AttachmentType.builder { _ -> -1 }.build()
    }

    val RACE_ID = REGISTRY.register("race_id") { _ ->
        AttachmentType.builder { _ -> Unknown.ID }.serialize(ResourceLocation.CODEC).copyOnDeath().build()
    }

    val BEHAVIOR_TAGS = REGISTRY.register("behavior_tags") { _ ->
        AttachmentType.builder { _ -> emptyList<ResourceLocation>() }.serialize(ResourceLocation.CODEC.listOf())
            .copyOnDeath().build()
    }

    val NUTRITION_STATE = REGISTRY.register("nutrition_state") { _ ->
        AttachmentType.builder { _ -> NutritionState() }.serialize(NutritionState.CODEC).copyOnDeath().build()
    }

    fun register(bus: IEventBus) {
        REGISTRY.register(bus)
    }
}