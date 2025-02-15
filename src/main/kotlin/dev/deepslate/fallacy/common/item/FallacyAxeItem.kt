package dev.deepslate.fallacy.common.item

import dev.deepslate.fallacy.common.item.data.ExtendedPropertiesLike
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.BlockTags
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.Tier
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.gameevent.GameEvent
import net.neoforged.neoforge.common.ItemAbilities
import net.neoforged.neoforge.common.ItemAbility
import java.util.*

class FallacyAxeItem(tier: Tier, properties: Properties, extendedProperties: ExtendedPropertiesLike) :
    FallacyDiggerItem(tier, BlockTags.MINEABLE_WITH_AXE, properties, extendedProperties) {

    companion object {
        fun playerHasShieldUseIntent(context: UseOnContext): Boolean {
            val player = context.player
            return context.hand == InteractionHand.MAIN_HAND && player!!.offhandItem
                .`is`(Items.SHIELD) && !player.isSecondaryUseActive
        }
    }

    override fun useOn(context: UseOnContext): InteractionResult {
        val level = context.level
        val pos = context.clickedPos
        val player = context.player

        if (playerHasShieldUseIntent(context)) return InteractionResult.PASS

        val state = this.evaluateNewBlockState(level, pos, player, level.getBlockState(pos), context)

        if (state.isEmpty) return InteractionResult.PASS

        val stack = context.itemInHand

        if (player is ServerPlayer) {
            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(player, pos, stack)
        }

        level.setBlock(pos, state.get(), 8 + 2 + 1)
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, state.get()))

        if (player != null) {
            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.hand))
        }

        return InteractionResult.sidedSuccess(level.isClientSide)
    }

    private fun evaluateNewBlockState(
        level: Level,
        pos: BlockPos,
        player: Player?,
        state: BlockState,
        context: UseOnContext
    ): Optional<BlockState> {
        val strippedState: Optional<BlockState> =
            Optional.ofNullable<BlockState>(state.getToolModifiedState(context, ItemAbilities.AXE_STRIP, false))

        if (strippedState.isPresent) {
            level.playSound(player, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0f, 1.0f)
            return strippedState
        }

        val scrapedState: Optional<BlockState> =
            Optional.ofNullable<BlockState>(state.getToolModifiedState(context, ItemAbilities.AXE_SCRAPE, false))

        if (scrapedState.isPresent) {
            level.playSound(player, pos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0f, 1.0f)
            level.levelEvent(player, 3005, pos, 0)
            return scrapedState
        }

        val waxState: Optional<BlockState> = Optional.ofNullable<BlockState>(
            state.getToolModifiedState(
                context,
                ItemAbilities.AXE_WAX_OFF,
                false
            )
        )

        if (waxState.isPresent) {
            level.playSound(player, pos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0f, 1.0f)
            level.levelEvent(player, 3004, pos, 0)
            return waxState
        }

        return Optional.empty<BlockState>()
    }

    override fun canPerformAction(stack: ItemStack, itemAbility: ItemAbility): Boolean {
        return ItemAbilities.DEFAULT_AXE_ACTIONS.contains(itemAbility)
    }
}