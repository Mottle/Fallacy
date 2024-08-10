package dev.deepslate.fallacy.common.block.crop

import net.minecraft.core.BlockPos
import net.minecraft.world.item.Item
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import net.minecraft.world.level.block.CropBlock as VanillaCropBlock


abstract class CropBlock(properties: Properties) : VanillaCropBlock(properties) {
    companion object {
        protected val SHAPE_BY_AGE: List<VoxelShape> = listOf(
            box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
            box(0.0, 0.0, 0.0, 16.0, 3.0, 16.0),
            box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
            box(0.0, 0.0, 0.0, 16.0, 5.0, 16.0),
            box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
            box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0),
            box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
            box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0)
        )

        val AGE: IntegerProperty = VanillaCropBlock.AGE
    }

    protected abstract val dead: Lazy<Block>
    protected abstract val seeds: Lazy<Item>

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape =
        SHAPE_BY_AGE[state.getValue(AGE)]

    
}