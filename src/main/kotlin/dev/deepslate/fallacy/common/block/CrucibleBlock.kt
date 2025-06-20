package dev.deepslate.fallacy.common.block

import com.mojang.serialization.MapCodec
import dev.deepslate.fallacy.common.block.entity.CrucibleEntity
import dev.deepslate.fallacy.common.block.entity.FallacyBlockEntities
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.capabilities.Capabilities
import kotlin.jvm.optionals.getOrNull

class CrucibleBlock(properties: Properties) : BaseEntityBlock(properties) {

    companion object {
        val CODEC: MapCodec<CrucibleBlock> = simpleCodec(::CrucibleBlock)
    }

    override fun codec(): MapCodec<out BaseEntityBlock> = CODEC

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? =
        FallacyBlockEntities.CRUCIBLE.create(pos, state)

    override fun <T : BlockEntity?> getTicker(level: Level, state: BlockState, blockEntityType: BlockEntityType<T>):
            BlockEntityTicker<T>? =
        createTickerHelper(blockEntityType, FallacyBlockEntities.CRUCIBLE.get(), CrucibleEntity::serverTick)

    override fun getRenderShape(state: BlockState): RenderShape = RenderShape.MODEL

    override fun useWithoutItem(
        state: BlockState, level: Level, pos: BlockPos, player: Player, hitResult: BlockHitResult
    ): InteractionResult {

        return InteractionResult.PASS
    }

    override fun useItemOn(
        stack: ItemStack,
        state: BlockState,
        level: Level,
        pos: BlockPos,
        player: Player,
        hand: InteractionHand,
        hitResult: BlockHitResult
    ): ItemInteractionResult {
        if (level.isClientSide) return ItemInteractionResult.SUCCESS
        if (stack.isEmpty) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION

        val entity = level.getBlockEntity(pos, FallacyBlockEntities.CRUCIBLE.get()).getOrNull()
        val inventory = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, hitResult.direction)
            ?: return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION
        val firstEmptySlot =
            (0 until CrucibleEntity.INVENTORY_SIZE).firstOrNull { inventory.getStackInSlot(it).isEmpty }
                ?: return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION

        if (!inventory.isItemValid(
                firstEmptySlot,
                stack
            )
        ) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION

        val ret = inventory.insertItem(firstEmptySlot, stack, false)
        player.setItemInHand(hand, ret)
        entity?.markForSync()

        return ItemInteractionResult.CONSUME
    }
}