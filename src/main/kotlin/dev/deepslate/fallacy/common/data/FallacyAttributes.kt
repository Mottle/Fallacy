package dev.deepslate.fallacy.common.data

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.thermodynamics.ThermodynamicsEngine
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.RangedAttribute
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object FallacyAttributes {
    private val registry = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, Fallacy.MOD_ID)

    val MAX_HUNGER: DeferredHolder<Attribute, Attribute> = registry.register("fallacy.max_hunger") { _ ->
        RangedAttribute("fallacy.attribute.name.player.max_hunger", 20.0, 10.0, 1024.0).setSyncable(true)
    }

    val MAX_THIRST: DeferredHolder<Attribute, Attribute> = registry.register("fallacy.max_thirst") { _ ->
        RangedAttribute("fallacy.attribute.name.player.max_thirst", 20.0, 10.0, 1024.0).setSyncable(true)
    }

    val STRENGTH: DeferredHolder<Attribute, Attribute> = registry.register("fallacy.strength") { _ ->
        RangedAttribute("fallacy.attribute.name.player.strength", 1.0, -1024.0, 1024.0).setSyncable(true)
    }

    val MAGIC_RESISTANCE: DeferredHolder<Attribute, Attribute> = registry.register("fallacy.magic_resistance") { _ ->
        RangedAttribute("fallacy.attribute.name.player.magic_resistance", 0.0, -1024.0, 1024.0).setSyncable(true)
    }

    val MAGIC_STRENGTH: DeferredHolder<Attribute, Attribute> = registry.register("fallacy.magic_strength") { _ ->
        RangedAttribute("fallacy.attribute.name.player.magic_strength", 0.0, -1024.0, 1024.0).setSyncable(true)
    }

    val MAX_BONE: DeferredHolder<Attribute, Attribute> = registry.register("fallacy.max_bone") { _ ->
        RangedAttribute("fallacy.attribute.name.player.max_bone", 10.0, 10.0, 1024.0).setSyncable(true)
    }

    val HEAT_RESISTANCE: DeferredHolder<Attribute, Attribute> = registry.register("fallacy.heat_resistance") { _ ->
        RangedAttribute("fallacy.attribute.name.player.heat_resistance", 0.0, -10.0, 10.0).setSyncable(true)
    }

    //默认初始体温
    val DEFAULT_BODY_HEAT: DeferredHolder<Attribute, Attribute> = registry.register("fallacy.default_body_heat") { _ ->
        RangedAttribute(
            "fallacy.attribute.name.player.default_body_heat",
            ThermodynamicsEngine.fromFreezingPoint(37).toDouble() + 0.5,
            ThermodynamicsEngine.MIN_HEAT.toDouble(),
            ThermodynamicsEngine.MAX_HEAT.toDouble()
        ).setSyncable(true)
    }

    fun init(bus: IEventBus) {
        registry.register(bus)
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