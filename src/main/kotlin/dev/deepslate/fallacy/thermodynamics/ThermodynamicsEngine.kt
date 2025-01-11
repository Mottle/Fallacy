package dev.deepslate.fallacy.thermodynamics

import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.thermodynamics.data.ChunkHeat
import dev.deepslate.fallacy.thermodynamics.data.LayerHeat
import dev.deepslate.fallacy.util.getEpitaxialHeat
import dev.deepslate.fallacy.util.hasHeat
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

interface ThermodynamicsEngine {
    fun getHeat(pos: BlockPos): Float

    fun updateHeat(oldState: BlockState, newState: BlockState, pos: BlockPos)

    fun scanChunk(chunkPos: ChunkPos)

    companion object {
        const val FREEZING_POINT = LayerHeat.FREEZING_POINT

        const val MIN_HEAT = LayerHeat.MIN_HEAT

        const val MAX_HEAT = LayerHeat.MAX_HEAT

        const val UNCHECKED_SIMPLE = LayerHeat.UNCHECKED_SIMPLE

        fun isUnchecked(heat: Float) = LayerHeat.isUnchecked(heat)

        fun fromFreezingPoint(heat: Float): Float = heat + FREEZING_POINT

        fun hasHeat(state: BlockState): Boolean = state.hasHeat() || VanillaHeat.hasHeat(state)

        fun getEpitaxialHeat(state: BlockState, level: Level, pos: BlockPos): Float {
            if (state.hasHeat()) return state.getEpitaxialHeat(level, pos)
            return VanillaHeat.getHeat(state)
        }

        fun initAndGetChunkHeat(chunkPos: ChunkPos, level: ServerLevel): ChunkHeat {
            val chunk = level.getChunk(chunkPos.x, chunkPos.z)
            val heat = chunk.getData(FallacyAttachments.CHUNK_HEAT)
            if(!heat.isInit()) {
                heat.init(level)
                chunk.isUnsaved = true
            }
            return heat
        }

        fun initAndGetChunkHeat(pos: BlockPos, level: ServerLevel): ChunkHeat = initAndGetChunkHeat(ChunkPos(pos), level)

        fun setChunkHeat(chunkHeat: ChunkHeat, chunkPos: ChunkPos, level: ServerLevel) {
            val chunk = level.getChunk(chunkPos.x, chunkPos.z)
            chunk.setData(FallacyAttachments.CHUNK_HEAT, chunkHeat)
        }
    }
}