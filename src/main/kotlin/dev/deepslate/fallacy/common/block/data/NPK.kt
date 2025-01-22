package dev.deepslate.fallacy.common.block.data

import dev.deepslate.fallacy.common.block.FertilityFarmBlock
import net.minecraft.world.level.block.state.BlockState

data class NPK(val n: Int, val p: Int, val k: Int) {

    companion object {
        fun zero() = NPK(0, 0, 0)
    }

    init {
        require(n >= 0 && p >= 0 && k >= 0) { "N-P-K values must be non-negative" }
        require(n <= 7 && p <= 7 && k <= 7) { "N-P-K values must be less that 8" }
    }

    fun canGrowAt(state: BlockState): Boolean {
        if (state.block !is FertilityFarmBlock) return false
        val stateN = state.getValue(FertilityFarmBlock.N)
        val stateP = state.getValue(FertilityFarmBlock.P)
        val stateK = state.getValue(FertilityFarmBlock.K)
        return stateN >= n && stateP >= p && stateK >= k
    }
}
