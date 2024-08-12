package dev.deepslate.fallacy.common.data

import com.mojang.serialization.Codec
import dev.deepslate.fallacy.Fallacy
import net.minecraft.network.codec.ByteBufCodecs
import net.neoforged.neoforge.registries.DeferredRegister

object FallacyDataComponents {
    private val REGISTER = DeferredRegister.createDataComponents(Fallacy.MOD_ID)

    val HYDRATION = REGISTER.registerComponentType("hydration") { builder ->
        builder.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT)
    }
}