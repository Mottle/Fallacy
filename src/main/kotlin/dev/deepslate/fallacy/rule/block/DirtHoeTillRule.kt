package dev.deepslate.fallacy.rule.block

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.block.FertilityFarmBlock
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
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

        val (check, stateGetter) = additionalTillMap()[event.state.block]!!

        if (!check(event.context)) return

        event.finalState = stateGetter(event.level, event.pos)
    }

    /**
     * @see net.minecraft.world.item.HoeItem.onlyIfAirAbove
     */
    private fun onlyIfAirAbove(context: UseOnContext): Boolean =
        context.clickedFace != Direction.DOWN && context.level.getBlockState(context.clickedPos.above())
            .isAir

    fun additionalTillMap(): Map<Block, Pair<(UseOnContext) -> Boolean, (LevelAccessor, BlockPos) -> BlockState>> =
        additionalTillMap.value

    private val additionalTillMap: Lazy<Map<Block, Pair<(UseOnContext) -> Boolean, (LevelAccessor, BlockPos) -> BlockState>>> =
        lazy {
            mapOf(
                Blocks.GRASS_BLOCK to Pair(
                    ::onlyIfAirAbove,
                    FertilityFarmBlock::tillDirt
                ),
                Blocks.DIRT_PATH to Pair(
                    ::onlyIfAirAbove,
                    FertilityFarmBlock::tillDirt
                ),
                Blocks.DIRT to Pair(
                    ::onlyIfAirAbove,
                    FertilityFarmBlock::tillDirt
                ),
                Blocks.COARSE_DIRT to Pair(
                    ::onlyIfAirAbove,
                    FertilityFarmBlock::tillDirt
                ),
            )
        }
}