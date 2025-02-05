package dev.deepslate.fallacy.weather

import net.minecraft.core.BlockPos
import net.minecraft.world.phys.Vec3

interface WindEngine {
    fun tick()

    fun getWindAt(pos: BlockPos): Vec3
}