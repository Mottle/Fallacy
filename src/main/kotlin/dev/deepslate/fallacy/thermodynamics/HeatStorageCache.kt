package dev.deepslate.fallacy.thermodynamics

import dev.deepslate.fallacy.thermodynamics.data.HeatStorage
import net.minecraft.world.level.ChunkPos

interface HeatStorageCache {
    fun queryPositive(chunkPos: ChunkPos): HeatStorage

    fun queryNegative(chunkPos: ChunkPos): HeatStorage
}