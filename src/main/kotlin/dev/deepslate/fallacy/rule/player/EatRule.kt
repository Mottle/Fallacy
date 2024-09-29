package dev.deepslate.fallacy.rule.player

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.capability.FallacyCapabilities
import net.minecraft.core.component.DataComponents
import net.minecraft.world.entity.player.Player
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent
import kotlin.math.max

object EatRule {
    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object Handler {
        @SubscribeEvent
        fun onEatStart(event: LivingEntityUseItemEvent.Start) {
            val entity = event.entity
            val item = event.item

            if (entity !is Player) return
            if (!item.has(DataComponents.FOOD)) return

            val diet = entity.getCapability(FallacyCapabilities.DIET)!!

            val multiple = diet.getEatDurationMultiple(item)
            event.duration = (event.duration * max(1f, multiple)).toInt()
        }

        @SubscribeEvent
        fun onEatFinished(event: LivingEntityUseItemEvent.Finish) {
            val entity = event.entity
            val item = event.item

            if (entity !is Player) return
            if (!item.has(DataComponents.FOOD)) return

            val diet = entity.getCapability(FallacyCapabilities.DIET)!!
            diet.eat(item)
        }
    }
}