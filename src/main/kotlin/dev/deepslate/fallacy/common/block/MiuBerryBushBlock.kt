package dev.deepslate.fallacy.common.block

import dev.deepslate.fallacy.common.data.FallacyItems
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.block.SweetBerryBushBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.phys.BlockHitResult

class MiuBerryBushBlock(properties: Properties) : SweetBerryBushBlock(properties) {

    companion object {
        val AGE: IntegerProperty = SweetBerryBushBlock.AGE
    }

    override fun mayPlaceOn(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean {
        if (!super.mayPlaceOn(state, level, pos)) return false

        val biome = (level as LevelAccessor).getBiome(pos)
        return !biome.`is`(Biomes.DESERT)
    }

    override fun useWithoutItem(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        val age = state.getValue(AGE)
        if(age < 1) return InteractionResult.PASS
        val dropAmount = 1 + level.random.nextInt(3) + if(age == 3) 1 else 0
        popResource(level, pos, ItemStack(FallacyItems.MIU_BERRIES.asItem(), dropAmount))
        level.playSound(
            null,
            pos,
            SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
            SoundSource.BLOCKS,
            1.0f,
            0.8f + level.random.nextFloat() * 0.4f
        )
        val newState = state.setValue(AGE, 1)
        level.setBlock(pos, newState, 2)
        level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, newState))
        return InteractionResult.sidedSuccess(level.isClientSide)
    }
}