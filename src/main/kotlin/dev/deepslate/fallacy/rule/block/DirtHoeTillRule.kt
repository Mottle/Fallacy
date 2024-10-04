package dev.deepslate.fallacy.rule.block

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.block.FallacyBlocks
import net.minecraft.core.Direction
import net.minecraft.world.item.HoeItem
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.gameevent.GameEvent
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.ItemAbilities
import net.neoforged.neoforge.event.level.BlockEvent.BlockToolModificationEvent

@EventBusSubscriber(modid = Fallacy.MOD_ID)
object DirtHoeTillRule {

    @SubscribeEvent
    fun onToolModifier(event: BlockToolModificationEvent) {
        if (event.itemAbility != ItemAbilities.HOE_TILL) return
        if (!additionalTillMap().contains(event.state.block)) return
        event.finalState = FallacyBlocks.FARMLAND.defaultState
    }

    /**
     * @see net.minecraft.world.item.HoeItem.onlyIfAirAbove
     */
    private fun onlyIfAirAbove(context: UseOnContext): Boolean =
        context.clickedFace != Direction.DOWN && context.level.getBlockState(context.clickedPos.above())
            .isAir

    /**
     * @see net.minecraft.world.item.HoeItem.changeIntoState
     */
    private fun changeIntoState(state: BlockState) = { context: UseOnContext ->
        context.level.setBlock(context.clickedPos, state, 11)
        context.level.gameEvent(GameEvent.BLOCK_CHANGE, context.clickedPos, GameEvent.Context.of(context.player, state))
    }

    fun additionalTillMap(): Map<Block, Pair<(UseOnContext) -> Boolean, (UseOnContext) -> Unit>> =
        additionalTillMap.value

    private val additionalTillMap: Lazy<Map<Block, Pair<(UseOnContext) -> Boolean, (UseOnContext) -> Unit>>> = lazy {
        mapOf(
            Blocks.GRASS_BLOCK to Pair(
                ::onlyIfAirAbove,
                changeIntoState(FallacyBlocks.FARMLAND.defaultState)
            ),
            Blocks.DIRT_PATH to Pair(
                ::onlyIfAirAbove,
                changeIntoState(FallacyBlocks.FARMLAND.defaultState)
            ),
            Blocks.DIRT to Pair(
                ::onlyIfAirAbove,
                changeIntoState(FallacyBlocks.FARMLAND.defaultState)
            ),
            Blocks.COARSE_DIRT to Pair(
                ::onlyIfAirAbove,
                changeIntoState(FallacyBlocks.FARMLAND.defaultState)
            ),
        )
    }
}