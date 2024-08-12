package dev.deepslate.fallacy.common.capability

import com.mojang.serialization.Codec
import dev.deepslate.fallacy.Fallacy
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries

object FallacyAttachments {
    private val ATTACHMENTS = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Fallacy.MOD_ID)

    val THIRST = ATTACHMENTS.register("thirst") { _ ->
        AttachmentType.builder { _ -> 20f }.serialize(Codec.FLOAT).build()
    }

//    val THIRST_TICKS = ATTACHMENTS.register("thirst_ticks") { _ ->
//        AttachmentType.builder { _ -> 0 }.build()
//    }

    fun register(bus: IEventBus) {
        ATTACHMENTS.register(bus)
    }
}