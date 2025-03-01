package dev.deepslate.fallacy.rule.block

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.block.FallacyBlocks
import dev.deepslate.fallacy.common.item.FallacyItemTags
import net.minecraft.core.Direction
import net.minecraft.tags.BlockTags
import net.minecraft.world.item.Items
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.LogicalSide
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent

@EventBusSubscriber(modid = Fallacy.MOD_ID)
object UseFlintAndSteelOnLogRule {
    @SubscribeEvent
    fun handleInteract(event: PlayerInteractEvent.RightClickBlock) {

        if (event.side == LogicalSide.CLIENT) return

        val level = event.level
        val item = event.itemStack
//        val hand = event.hand

//        if(hand != InteractionHand.MAIN_HAND) return
        if (!item.tags.anyMatch { it == FallacyItemTags.FIRE_STARTER } && !item.`is`(Items.FLINT_AND_STEEL)) return

        val state = level.getBlockState(event.pos)

        if (!state.tags.anyMatch { it == BlockTags.LOGS }) return
        if (event.face != Direction.UP) return

        val newState = FallacyBlocks.BURNING_LOG.get().defaultBlockState()
        level.setBlockAndUpdate(event.pos, newState)
    }
}