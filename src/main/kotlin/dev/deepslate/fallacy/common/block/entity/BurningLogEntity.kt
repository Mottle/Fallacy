package dev.deepslate.fallacy.common.block.entity

import dev.deepslate.fallacy.common.block.FallacyBlockTags
import dev.deepslate.fallacy.common.block.FallacyBlocks
import dev.deepslate.fallacy.util.TickHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class BurningLogEntity(type: BlockEntityType<*>, pos: BlockPos, state: BlockState) :
    TickDurationBlockEntity(type, pos, state) {

    companion object {
        const val TICK_INTERVAL = 10

        val MAX_BURNING_TICKS = TickHelper.minute(2)

        fun serverTick(level: Level, pos: BlockPos, state: BlockState, entity: BurningLogEntity) {
            if (!TickHelper.checkServerTickRate(TICK_INTERVAL)) return
            if (!checkAroundState(level, pos)) {
                entity.reset()
                entity.markForSync()
                return
            }

            if (entity.durationTicks >= MAX_BURNING_TICKS && level.isAreaLoaded(pos, 0)) {
                level.setBlockAndUpdate(pos, FallacyBlocks.BURNOUT_LOG.get().defaultBlockState())
            }
        }

//        private val shuffledCheck = Direction.entries.toTypedArray().shuffle()

        fun checkAroundState(level: Level, pos: BlockPos): Boolean {
            val directions = Direction.entries.map(pos::relative)
            val around = directions.map { relativePos ->
                checkState(level, relativePos)
            }

            return around.all { it }
        }

        fun checkState(level: Level, pos: BlockPos): Boolean {
            if (!level.isAreaLoaded(pos, 0)) return false

            val block = level.getBlockState(pos)

            if (!block.isCollisionShapeFullBlock(level, pos)) return false
            if (block.tags.anyMatch { it == BlockTags.DIRT || it == FallacyBlockTags.COAL || it == BlockTags.LOGS }) return true
            if (block.`is`(FallacyBlocks.BURNING_LOG)) return true
            return false
        }
    }

    init {
        reset()
//        markForSync()
    }
}