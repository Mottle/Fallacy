package dev.deepslate.fallacy.common.block

import dev.deepslate.fallacy.common.block.data.NPK
import dev.deepslate.fallacy.util.extension.setting
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FarmBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.gameevent.GameEvent
import net.neoforged.neoforge.common.FarmlandWaterManager

open class NPKFarmBlock(
    properties: Properties,
    val baseDirt: Holder<Block> = BuiltInRegistries.BLOCK.getHolder(BuiltInRegistries.BLOCK.getKey(Blocks.DIRT)).get()
) : FarmBlock(properties) {
    companion object {
        val N = FallacyStateProperties.N

        val P = FallacyStateProperties.P

        val K = FallacyStateProperties.K

        /**
         * @see net.minecraft.world.level.block.FarmBlock.isNearWater
         */
        protected fun isNearWater(level: LevelReader, pos: BlockPos): Boolean {
            val state = level.getBlockState(pos);
            for (blockPos in BlockPos.betweenClosed(pos.offset(-4, 0, -4), pos.offset(4, 1, 4))) {
                if (state.canBeHydrated(level, pos, level.getFluidState(blockPos), blockPos)) {
                    return true
                }
            }

            return FarmlandWaterManager.hasBlockWaterTicket(level, pos)
        }

        /**
         * @see net.minecraft.world.level.block.FarmBlock.shouldMaintainFarmland
         */
        protected fun shouldMaintainFarmland(level: BlockGetter, pos: BlockPos) =
            level.getBlockState(pos.above()).`is`(BlockTags.MAINTAINS_FARMLAND);
    }

    init {
        registerDefaultState(super.defaultBlockState().setValue(N, 0).setValue(P, 0).setValue(K, 0))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        super.createBlockStateDefinition(builder)
        builder.add(N, P, K)
    }

    protected fun applyNPK(state: BlockState, npk: NPK): BlockState =
        state.setValue(N, npk.n).setValue(P, npk.p).setValue(K, npk.k)

    override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
        val biomeNPK = context.level.getBiome(context.clickedPos).value().setting.npk
        val default = applyNPK(defaultBlockState(), biomeNPK)

        return if (default.canSurvive(context.level, context.clickedPos)) {
            default
        } else {
            null
        }
    }

    override fun fallOn(
        level: Level,
        state: BlockState,
        pos: BlockPos,
        entity: Entity,
        fallDistance: Float
    ) {
        entity.causeFallDamage(fallDistance, 1.0F, entity.damageSources().fall())
    }

    /**
     * @see net.minecraft.world.level.block.FarmBlock.turnToDirt
     */
    protected open fun turnToDirt(state: BlockState, level: Level, pos: BlockPos) {
        val dirtState = pushEntitiesUp(state, baseDirt.value().defaultBlockState(), level, pos)
        level.setBlockAndUpdate(pos, dirtState)
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(null, dirtState))
    }

    //土壤肥力退化
    protected open fun degenerate(state: BlockState, level: Level, pos: BlockPos): BlockState {
        if (level.random.nextInt(5) != 0) return state
        val choice = level.random.nextInt(3)
        val property = when (choice) {
            0 -> N
            1 -> P
            else -> K
        }
        val value = state.getValue(property)
        val biomeSetting = level.getBiome(pos).value().setting
        val biomeProperty = when (choice) {
            0 -> biomeSetting.npk.n
            1 -> biomeSetting.npk.p
            else -> biomeSetting.npk.k
        }

        if (value <= biomeProperty) return state

        val nextValue = (value - 1).coerceAtLeast(0)

        return state.setValue(property, nextValue)
    }

    override fun randomTick(
        state: BlockState,
        level: ServerLevel,
        pos: BlockPos,
        random: RandomSource
    ) {
        val state = degenerate(state, level, pos)
        val moisture = state.getValue(MOISTURE)

        if (!isNearWater(level, pos) && !level.isRainingAt(pos.above())) {
            if (moisture > 0) {
                level.setBlock(pos, state.setValue(MOISTURE, Integer.valueOf(moisture - 1)), 2)
            } else if (!shouldMaintainFarmland(level, pos)) {
                turnToDirt(state, level, pos)
            }
        } else if (moisture < 7) {
            level.setBlock(pos, state.setValue(MOISTURE, Integer.valueOf(7)), 2)
        }
    }
}