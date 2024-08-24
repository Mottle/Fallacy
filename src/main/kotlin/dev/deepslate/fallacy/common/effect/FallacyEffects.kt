package dev.deepslate.fallacy.common.effect

import dev.deepslate.fallacy.Fallacy
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister

object FallacyEffects {
    val MOB_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Fallacy.MOD_ID)

    fun init(bus: IEventBus) {
        MOB_EFFECTS.register(bus)
    }

    val DEHYDRATION = MOB_EFFECTS.register("dehydration") { _ ->
        val id = Fallacy.id("effect.dehydration")
        Dehydration().addAttributeModifier(
            Attributes.ATTACK_SPEED,
            id,
            -0.5,
            AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        ).addAttributeModifier(
            Attributes.MOVEMENT_SPEED,
            id,
            -0.2,
            AttributeModifier.Operation.ADD_MULTIPLIED_BASE
        )
    }
}