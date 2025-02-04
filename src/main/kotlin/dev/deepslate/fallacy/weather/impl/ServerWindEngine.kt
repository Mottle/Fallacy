package dev.deepslate.fallacy.weather.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.util.extension.internalWindEngine
import dev.deepslate.fallacy.weather.WindEngine
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.level.LevelEvent

class ServerWindEngine(val level: ServerLevel) : WindEngine {

    companion object {
        const val BOUND_WIND_STRENGTH = 0.1
    }

    override fun tick() {
        val entities = level.allEntities.filter { it is LivingEntity || it is ItemEntity }
        entities.forEach(::tickEntity)
    }

    var globalWindVec: Vec3 = Vec3.ZERO

    override fun getWindAt(pos: BlockPos): Vec3 {
        return globalWindVec
    }

    private fun tickEntity(entity: Entity) {
        val pos = entity.blockPosition()
        val wind = getWindAt(pos)

        if (wind.length() > BOUND_WIND_STRENGTH && level.canSeeSky(pos)) {
            entity.addDeltaMovement(wind)
        }
    }

    @EventBusSubscriber(modid = Fallacy.Companion.MOD_ID)
    object Handler {
        @SubscribeEvent
        fun onServerLevelLoad(event: LevelEvent.Load) {
            val level = event.level as Level

            if (level.isClientSide) return
            if (level.dimension() != Level.OVERWORLD) return

            val serverLevel = level as ServerLevel
            serverLevel.internalWindEngine = ServerWindEngine(serverLevel)
        }
    }
}