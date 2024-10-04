package dev.deepslate.fallacy.common.block.data

import dev.deepslate.fallacy.common.block.NPKFarmBlock
import net.minecraft.world.level.block.state.BlockState

data class NPK(val n: Int, val p: Int, val k: Int) {
    init {
        require(n >= 0 && p >= 0 && k >= 0) { "N-P-K values must be non-negative" }
        require(n <= 7 && p <= 7 && k <= 7) { "N-P-K values must be less that 8" }
    }

    fun canGrowAt(state: BlockState): Boolean {
        if (state.block !is NPKFarmBlock) return false
        val stateN = state.getValue(NPKFarmBlock.N)
        val stateP = state.getValue(NPKFarmBlock.P)
        val stateK = state.getValue(NPKFarmBlock.K)
        return stateN >= n && stateP >= p && stateK >= k
    }
}
