package dev.deepslate.fallacy.thermodynamics

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.block.BlockWithThermalConductivity
import dev.deepslate.fallacy.inject.FallacyThermodynamicsExtension
import dev.deepslate.fallacy.util.*
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.tick.LevelTickEvent
import net.neoforged.neoforge.event.tick.PlayerTickEvent
import kotlin.math.absoluteValue

abstract class ThermodynamicsEngine {
    abstract fun getHeat(pos: BlockPos): Int

    abstract fun checkBlock(pos: BlockPos)

    abstract fun scanChunk(chunkPos: ChunkPos)

    abstract fun runUpdates()

    abstract val level: Level

    abstract val cache: HeatStorageCache

    companion object {
        const val FREEZING_POINT = 273

        const val MIN_HEAT = 0

        const val MAX_HEAT = 8192 - 1

        const val MIN_BIOME_HEAT = 273 - 120

        const val MAX_BIOME_HEAT = 273 + 200

        fun fromFreezingPoint(heat: Int): Int = heat + FREEZING_POINT

        fun isHeatSource(state: BlockState): Boolean = state.isHeatSource() || VanillaHeat.hasHeat(state)

        fun getEpitaxialHeat(state: BlockState, level: Level, pos: BlockPos): Int {
            if (state.isHeatSource()) return state.getEpitaxialHeat(level, pos)
            return VanillaHeat.getHeat(state)
        }

        fun getIntrinsicHeat(state: BlockState, level: Level, pos: BlockPos): Int {
            if (state.isHeatSource()) return state.getIntrinsicHeat(level, pos)
            return VanillaHeat.getHeat(state)
        }

        fun getThermalConductivity(state: BlockState, level: Level, pos: BlockPos): Float {
            //re-fix: 自己吓自己
            //fix: 不知道为啥，不加会影响increase，decrease
//            if (isHeatSource(state)) return 1f
            if (state.block is BlockWithThermalConductivity) return state.getThermalConductivity(level, pos)
            return VanillaHeat.getThermalConductivity(state)
        }

        fun getEngine(level: ServerLevel): ThermodynamicsEngine =
            (level as FallacyThermodynamicsExtension).`fallacy$getThermodynamicsEngine`()

        fun getHeat(level: Level, pos: BlockPos): Int {
            val engine = getEngine(level as ServerLevel)
            return engine.getHeat(pos)
        }

        fun getHeatAt(entity: Entity): Int {
            val pos = entity.blockPosition()
            val level = entity.level()

            if (level.isClientSide) return 0

            return getHeat(level, pos)
        }

        fun hasDifferentHeatProperties(from: BlockState, to: BlockState, level: Level, pos: BlockPos): Boolean {
            if (from == to) return false

            val fromHeat = getEpitaxialHeat(from, level, pos)
            val toHeat = getEpitaxialHeat(to, level, pos)
            if (fromHeat != toHeat) return true

            val fromConductivity = getThermalConductivity(from, level, pos)
            val toConductivity = getThermalConductivity(to, level, pos)

            return fromConductivity != toConductivity
        }
    }


    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object Handler {
        @SubscribeEvent
        fun onServerLevelTick(event: LevelTickEvent.Post) {
            if (event.level.isClientSide) return
            if (!TickHelper.checkServerTickRate(TickHelper.second(2))) return

            val level = event.level as ServerLevel
            val engine = getEngine(level)
            engine.runUpdates()
        }

        //to lagged
//        @SubscribeEvent(priority = EventPriority.LOWEST)
//        fun onChunkLoad(event: ChunkEvent.Load) {
//            if(event.level.isClientSide) return
//            val chunk = event.chunk
//            val engine = getEngine(event.level as ServerLevel)
//            engine.scanChunk(chunk.pos)
//        }

        private fun getDistance(p1: ChunkPos, p2: ChunkPos): Int =
            (p1.x - p2.x).absoluteValue + (p1.z - p2.z).absoluteValue

        @SubscribeEvent
        fun onPlayerTick(event: PlayerTickEvent.Post) {
            if (event.entity.level().isClientSide) return
            if (!TickHelper.checkServerTickRate(TickHelper.second(2))) return

            val player = event.entity as ServerPlayer

            if (player.isSpectator) return

            val level = player.level() as ServerLevel
            val centerChunkPos = player.chunkPosition()
            val engine = getEngine(level)

            for (dx in -2..2) for (dz in -2..2) {
                val selectedChunkPos = ChunkPos(centerChunkPos.x + dx, centerChunkPos.z + dz)

                if (getDistance(selectedChunkPos, centerChunkPos) > 2) continue
                engine.scanChunk(selectedChunkPos)
            }
        }
    }
}