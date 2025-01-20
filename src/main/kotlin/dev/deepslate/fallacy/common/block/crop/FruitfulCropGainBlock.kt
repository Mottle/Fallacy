package dev.deepslate.fallacy.common.block.crop

import net.minecraft.core.Holder
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.level.storage.loot.LootParams

open class FruitfulCropGainBlock(properties: Properties, val cropItem: Holder<Item>) : Block(properties) {

    companion object {
        val AMOUNT: IntegerProperty = IntegerProperty.create("amount", 0, 15)
    }

    init {
        registerDefaultState(defaultBlockState().setValue(AMOUNT, 0))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        super.createBlockStateDefinition(builder)
        builder.add(AMOUNT)
    }

    fun getAmount(state: BlockState): Int = state.getValue(AMOUNT)

    fun setAmount(state: BlockState, amount: Int): BlockState = state.setValue(AMOUNT, amount)

    fun getCropItemStack(state: BlockState): ItemStack {
        return ItemStack(cropItem.value(), getAmount(state))
    }

    override fun getDrops(
        state: BlockState,
        params: LootParams.Builder
    ): List<ItemStack?> {
        return super.getDrops(state, params) + getCropItemStack(state)
    }
}