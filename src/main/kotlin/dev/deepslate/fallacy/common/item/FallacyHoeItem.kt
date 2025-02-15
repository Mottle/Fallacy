package dev.deepslate.fallacy.common.item

import com.mojang.datafixers.util.Pair
import dev.deepslate.fallacy.common.item.data.ExtendedPropertiesLike
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.BlockTags
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.HoeItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.item.context.UseOnContext
import net.neoforged.neoforge.common.ItemAbilities
import net.neoforged.neoforge.common.ItemAbility
import java.util.function.Consumer
import java.util.function.Predicate

class FallacyHoeItem(tier: Tier, properties: Properties, extendedProperties: ExtendedPropertiesLike) :
    FallacyDiggerItem(tier, BlockTags.MINEABLE_WITH_HOE, properties, extendedProperties) {
    override fun useOn(context: UseOnContext): InteractionResult {
        val level = context.level
        val pos = context.clickedPos
        val toolModifiedState =
            level.getBlockState(pos).getToolModifiedState(context, ItemAbilities.HOE_TILL, false)
        val pair =
            if (toolModifiedState == null) null else Pair.of<Predicate<UseOnContext>, Consumer<UseOnContext>>(
                Predicate { ctx: UseOnContext -> true },
                HoeItem.changeIntoState(toolModifiedState)
            )
        if (pair == null) return InteractionResult.PASS

        val predicate = pair.getFirst()
        val consumer = pair.getSecond()

        if (predicate.test(context)) {
            val player = context.player
            level.playSound(player, pos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0f, 1.0f)
            if (!level.isClientSide) {
                consumer.accept(context)
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
        return ItemAbilities.DEFAULT_HOE_ACTIONS.contains(itemAbility)
    }
}