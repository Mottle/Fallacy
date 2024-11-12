package dev.deepslate.fallacy.weather.wind

import net.minecraft.core.BlockPos
import net.minecraft.world.phys.Vec3

interface WindEngine {
    fun tick()

    fun getWindAt(pos: BlockPos): Vec3
}