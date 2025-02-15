package dev.deepslate.fallacy.common.item

import dev.deepslate.fallacy.common.item.data.ExtendedPropertiesLike
import net.minecraft.core.Direction
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.BlockTags
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.gameevent.GameEvent
import net.neoforged.neoforge.common.ItemAbilities
import net.neoforged.neoforge.common.ItemAbility

class FallacyShovelItem(tier: Tier, properties: Properties, extendedProperties: ExtendedPropertiesLike) :
    FallacyDiggerItem(tier, BlockTags.MINEABLE_WITH_SHOVEL, properties, extendedProperties) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val level = context.level
        val pos = context.clickedPos
        val state = level.getBlockState(pos)

        if (context.clickedFace == Direction.DOWN) return InteractionResult.PASS

        val player = context.player
        val flattenState = state.getToolModifiedState(context, ItemAbilities.SHOVEL_FLATTEN, false)
        var finalState: BlockState? = null

        if (flattenState != null && level.getBlockState(pos.above()).isAir) {
            level.playSound(player, pos, SoundEvents.SHOVEL_FLATTEN, SoundSource.BLOCKS, 1.0f, 1.0f)
            finalState = flattenState
        } else if ((state.getToolModifiedState(context, ItemAbilities.SHOVEL_DOUSE, false)
                .also { finalState = it }) != null
        ) {
            if (!level.isClientSide()) {
                level.levelEvent(null, 1009, pos, 0)
            }
        }

        if (finalState != null) {
            if (!level.isClientSide) {
                level.setBlock(pos, finalState, 8 + 2 + 1)
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, finalState))
                if (player != null) {
                    context.itemInHand.hurtAndBreak(1, player, LivingEntity.getSlotForHand(context.hand))
                }
            }

            return InteractionResult.sidedSuccess(level.isClientSide)
        } else {
            return InteractionResult.PASS
        }
    }

    override fun canPerformAction(stack: ItemStack, itemAbility: ItemAbility): Boolean {
        return ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(itemAbility)
    }
}