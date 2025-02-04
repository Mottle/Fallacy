package dev.deepslate.fallacy.hydration

import dev.deepslate.fallacy.common.capability.FallacyCapabilities
import dev.deepslate.fallacy.common.capability.thirst.IThirst
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.util.TickHelper
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.phys.HitResult

object Drink {

    private const val INTERVAL_TICKS = 10

    private fun checkIntervalAndUpdateLastDrinkTick(level: Level, player: Player): Boolean {
        val lastDrinkTick = player.getData(FallacyAttachments.LAST_DRINK_TICK)
        val tick = if (level.isClientSide) TickHelper.currentClientTick else level.server!!.tickCount

        if (tick - lastDrinkTick >= INTERVAL_TICKS) {
            player.setData(FallacyAttachments.LAST_DRINK_TICK, tick)
            return true
        }

        return false
    }

    fun attemptDrink(level: Level, player: Player): InteractionResult {
        val hit = Item.getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY)

        if (hit.type != HitResult.Type.BLOCK) return InteractionResult.PASS

        val pos = hit.blockPos
        val state = if (level.isAreaLoaded(pos, 0)) level.getBlockState(pos) else Blocks.AIR.defaultBlockState()
        val fluid = state.fluidState.type

        if (!fluid.isSame(Fluids.WATER)) return InteractionResult.PASS
        if (!checkIntervalAndUpdateLastDrinkTick(level, player)) return InteractionResult.FAIL

        val thirst = player.getCapability(FallacyCapabilities.THIRST)!!

        if (thirst.value >= thirst.max) return InteractionResult.PASS
        if (!level.isClientSide) {
            doDrink(level, player, thirst, state, pos)
        }

        return InteractionResult.SUCCESS
    }

    fun doDrink(level: Level, player: Player, thirst: IThirst, state: BlockState, pos: BlockPos) {
        thirst.drink(1f)
        level.playSound(null, pos, SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 1.0f, 1.0f);
    }
}