package dev.deepslate.fallacy.weather.wind

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3

class ServerWindEngine(val level: ServerLevel) : WindEngine {
    override fun tick() {
//        val entities = level.allEntities.filter { it is LivingEntity || it is ItemEntity }
//        entities.forEach(::tickEntity)
    }

    var globalWindVec: Vec3 = Vec3.ZERO

    override fun getWindAt(pos: BlockPos): Vec3 {
        return globalWindVec
    }

    private fun tickEntity(entity: Entity) {
        val pos = entity.blockPosition()
        val wind = getWindAt(pos)

        if (wind.length() > 0.1 && level.canSeeSky(pos)) {
            entity.addDeltaMovement(wind)
        }
    }
}