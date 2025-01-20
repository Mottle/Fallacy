package dev.deepslate.fallacy.common.block.crop

import dev.deepslate.fallacy.common.block.FallacyStateProperties
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.IntegerProperty

//part start from 0
open class TallFruitfulCropGainBlock(properties: Properties, cropItem: Holder<Item>) :
    FruitfulCropGainBlock(properties, cropItem) {

    companion object {
        const val MAX_HEIGHT = 8

//        val PARTS = (1..7).map { IntegerProperty.create("part_$it", 0, it) }
//
//        fun part(count: Int) = PARTS[count - 2]

        val PART: IntegerProperty = FallacyStateProperties.PART_2
    }

    init {
//        require(partCount > 1)
//        require(partCount <= MAX_HEIGHT)
        registerDefaultState(defaultBlockState().setValue(PART, 0))
    }

    open val partCount: Int = 2

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block?, BlockState?>) {
        super.createBlockStateDefinition(builder)
        builder.add(PART)
    }

    override fun canSurvive(
        state: BlockState,
        level: LevelReader,
        pos: BlockPos
    ): Boolean {
        val topState = level.getBlockState(pos.above())
        return topState.`is`(Blocks.VOID_AIR) || topState.`is`(Blocks.AIR) || topState.`is`(Blocks.CAVE_AIR)
    }

    override fun onPlace(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        oldState: BlockState,
        movedByPiston: Boolean
    ) {
        super.onPlace(state, level, pos, oldState, movedByPiston)

        if (state.getValue(PART) != 0) return
        for (part in 1..partCount - 1) {
            val x = pos.x
            val y = pos.y + part
            val z = pos.z
            val p = BlockPos(x, y, z)
            val nState = defaultBlockState().setValue(PART, part)

            level.setBlockAndUpdate(p, nState)
        }
    }

    override fun onRemove(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        newState: BlockState,
        movedByPiston: Boolean
    ) {
        if (!state.hasProperty(PART)) return

        val part = state.getValue(PART)
        val startY = pos.y - part
        val endY = startY + partCount - part - 1

        for (y in startY..endY) {
            if (y == pos.y) continue
            val pos = BlockPos(pos.x, y, pos.z)
            val partState = level.getBlockState(pos)
            if (!isSame(state, partState)) continue
            level.removeBlock(pos, false)
        }

        super.onRemove(state, level, pos, newState, movedByPiston)
    }

    fun isSame(self: BlockState, other: BlockState): Boolean =
        self.block is TallFruitfulCropGainBlock && other.block is TallFruitfulCropGainBlock
}