package dev.deepslate.fallacy.rule.item

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.item.component.FallacyDataComponents
import dev.deepslate.fallacy.util.extension.extendedProperties
import net.minecraft.world.item.ItemStack
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent

@EventBusSubscriber(modid = Fallacy.MOD_ID)
object DisableItemRule {

    private val set = setOf(FallacyDataComponents.OUTDATED, FallacyDataComponents.DEPRECATED)

    private inline fun tryDisableItem(stack: ItemStack, callback: () -> Unit) {
        if (set.any { stack.has(it) }) {
            callback()
            return
        }

        val extendedProperties = stack.item.extendedProperties ?: return
        if (extendedProperties.isDeprecated) callback()
    }

    @SubscribeEvent
    fun onPlayerUse(event: LivingEntityUseItemEvent.Start) {
        tryDisableItem(event.item) { event.isCanceled = true }
    }

    @SubscribeEvent
    fun onPlayerInteractBlock(event: PlayerInteractEvent.RightClickBlock) {
        val item = event.entity.getItemInHand(event.hand)
        tryDisableItem(item) { event.isCanceled = true }
    }

    @SubscribeEvent
    fun onPlayerInteractBlock(event: PlayerInteractEvent.LeftClickBlock) {
        val item = event.entity.getItemInHand(event.hand)
        tryDisableItem(item) { event.isCanceled = true }
    }

    @SubscribeEvent
    fun onPlayerInteractBlock(event: PlayerInteractEvent.EntityInteract) {
        val item = event.entity.getItemInHand(event.hand)
        tryDisableItem(item) { event.isCanceled = true }
    }

    @SubscribeEvent
    fun onPlayerInteractBlock(event: PlayerInteractEvent.RightClickItem) {
        val item = event.entity.getItemInHand(event.hand)
        tryDisableItem(item) { event.isCanceled = true }
    }

    @SubscribeEvent
    fun onPlayerInteractBlock(event: PlayerInteractEvent.EntityInteractSpecific) {
        val item = event.entity.getItemInHand(event.hand)
        tryDisableItem(item) { event.isCanceled = true }
    }
}