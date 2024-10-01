package dev.deepslate.fallacy.common.data

import dev.deepslate.fallacy.Fallacy
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.RangedAttribute
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent
import net.neoforged.neoforge.registries.DeferredRegister

object FallacyAttributes {
    val ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, Fallacy.MOD_ID)

    val MAX_HUNGER = ATTRIBUTES.register("fallacy.max_hunger") { _ ->
        RangedAttribute("fallacy.attribute.name.player.max_hunger", 20.0, 10.0, 1024.0).setSyncable(true)
    }

    val MAX_THIRST = ATTRIBUTES.register("fallacy.max_thirst") { _ ->
        RangedAttribute("fallacy.attribute.name.player.max_thirst", 20.0, 10.0, 1024.0).setSyncable(true)
    }

    val STRENGTH = ATTRIBUTES.register("fallacy.strength") { _ ->
        RangedAttribute("fallacy.attribute.name.player.strength", 1.0, -1024.0, 1024.0).setSyncable(true)
    }

    val MAGIC_RESISTANCE = ATTRIBUTES.register("fallacy.magic_resistance") { _ ->
        RangedAttribute("fallacy.attribute.name.player.magic_resistance", 0.0, -1024.0, 1024.0).setSyncable(true)
    }

    val MAGIC_STRENGTH = ATTRIBUTES.register("fallacy.magic_strength") { _ ->
        RangedAttribute("fallacy.attribute.name.player.magic_strength", 0.0, -1024.0, 1024.0).setSyncable(true)
    }

    val MAX_BONE = ATTRIBUTES.register("fallacy.max_bone") { _ ->
        RangedAttribute("fallacy.attribute.name.player.max_bone", 10.0, 10.0, 1024.0).setSyncable(true)
    }

    fun init(bus: IEventBus) {
        ATTRIBUTES.register(bus)
    }

    @EventBusSubscriber(modid = Fallacy.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    object ModbusHandler {
        @SubscribeEvent
        fun handle(event: EntityAttributeModificationEvent) {
            event.add(EntityType.PLAYER, MAX_HUNGER)
            event.add(EntityType.PLAYER, MAX_THIRST)
            event.add(EntityType.PLAYER, STRENGTH)
            event.add(EntityType.PLAYER, MAGIC_RESISTANCE)
            event.add(EntityType.PLAYER, MAGIC_STRENGTH)
            event.add(EntityType.PLAYER, MAX_BONE)
        }
    }
}