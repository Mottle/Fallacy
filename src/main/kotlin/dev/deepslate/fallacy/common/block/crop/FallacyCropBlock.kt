package dev.deepslate.fallacy.common.block.crop

import dev.deepslate.fallacy.common.block.FallacyStateProperties
import dev.deepslate.fallacy.common.block.data.NPK
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import net.neoforged.neoforge.common.CommonHooks


abstract class FallacyCropBlock(properties: Properties, val npkRequired: NPK) : CropBlock(properties) {
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

        val AGE: IntegerProperty = FallacyStateProperties.AGE
    }

    protected val dead: Holder<Block>? = null

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape =
        SHAPE_BY_AGE[state.getValue(AGE)]

    override fun mayPlaceOn(
        state: BlockState,
        level: BlockGetter,
        pos: BlockPos
    ): Boolean {
        return super.mayPlaceOn(state, level, pos)
    }

    /**
     * @see net.minecraft.world.level.block.CropBlock.randomTick
     */
    override fun randomTick(
        state: BlockState,
        level: ServerLevel,
        pos: BlockPos,
        random: RandomSource
    ) {
        // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (!level.isAreaLoaded(pos, 1)) return
        if (level.getRawBrightness(pos, 0) >= 9) {
            val age = this.getAge(state);
            if (age < this.maxAge && npkRequired.canGrowAt(state)) {
                val growthSpeed = getGrowthSpeed(state, level, pos);
                if (CommonHooks.canCropGrow(
                        level,
                        pos,
                        state,
                        random.nextInt((25.0F / growthSpeed).toInt() + 1) == 0
                    )
                ) {
                    level.setBlock(pos, this.getStateForAge(age + 1), 2);
                    CommonHooks.fireCropGrowPost(level, pos, state);
                }
            }
        }
    }
}