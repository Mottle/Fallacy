package dev.deepslate.fallacy.rule.block

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.util.extendedProperties
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent

@EventBusSubscriber(modid = Fallacy.MOD_ID)
object RemoveBlockRule {
    @SubscribeEvent
    fun onPlayerUse(event: LivingEntityUseItemEvent.Start) {
        val extendedProperties = event.item.item.extendedProperties ?: return
        if (extendedProperties.isDeprecated) event.isCanceled = true
    }

    @SubscribeEvent
    fun onPlayerInteractBlock(event: PlayerInteractEvent.RightClickBlock) {
        val item = event.entity.getItemInHand(event.hand).item
        val extendedProperties = item.extendedProperties ?: return
        if (extendedProperties.isDeprecated) event.isCanceled = true
    }

    @SubscribeEvent
    fun onPlayerInteractBlock(event: PlayerInteractEvent.LeftClickBlock) {
        val item = event.entity.getItemInHand(event.hand).item
        val extendedProperties = item.extendedProperties ?: return
        if (extendedProperties.isDeprecated) event.isCanceled = true
    }

    @SubscribeEvent
    fun onPlayerInteractBlock(event: PlayerInteractEvent.EntityInteract) {
        val item = event.entity.getItemInHand(event.hand).item
        val extendedProperties = item.extendedProperties ?: return
        if (extendedProperties.isDeprecated) event.isCanceled = true
    }

    @SubscribeEvent
    fun onPlayerInteractBlock(event: PlayerInteractEvent.RightClickItem) {
        val item = event.entity.getItemInHand(event.hand).item
        val extendedProperties = item.extendedProperties ?: return
        if (extendedProperties.isDeprecated) event.isCanceled = true
    }

    @SubscribeEvent
    fun onPlayerInteractBlock(event: PlayerInteractEvent.EntityInteractSpecific) {
        val item = event.entity.getItemInHand(event.hand).item
        val extendedProperties = item.extendedProperties ?: return
        if (extendedProperties.isDeprecated) event.isCanceled = true
    }
}