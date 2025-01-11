package dev.deepslate.fallacy.thermodynamics.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.inject.item.FallacyThermodynamicsExtension
import dev.deepslate.fallacy.thermodynamics.ThermodynamicsEngine
import dev.deepslate.fallacy.thermodynamics.data.Temperature
import dev.deepslate.fallacy.util.TickHelper
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.saveddata.SavedData
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.level.LevelEvent
import net.neoforged.neoforge.event.tick.LevelTickEvent
import net.neoforged.neoforge.event.tick.PlayerTickEvent
import java.util.*
import kotlin.math.absoluteValue

open class EnvironmentThermodynamicsEngine(val level: Level) : ThermodynamicsEngine {

    companion object {
        const val BIOME_BASE_CELSIUS = 15f

        fun getEngine(level: ServerLevel): ThermodynamicsEngine =
            (level as FallacyThermodynamicsExtension).`fallacy$getThermodynamicsEngine`()

        fun getEnvironmentEngineOrNull(level: ServerLevel): EnvironmentThermodynamicsEngine? =
            (level as? FallacyThermodynamicsExtension)?.`fallacy$getThermodynamicsEngine`() as? EnvironmentThermodynamicsEngine
    }

    private val chunkScanner = ChunkScanner(engine = this)

    private val heatMaintainer = HeatMaintainer(this)

    override fun getHeat(pos: BlockPos): Float {
        val savedHeat = ThermodynamicsEngine.initAndGetChunkHeat(pos, level as ServerLevel).getHeatBy(pos, level)
        val deltaHeat = getWeatherHeatDelta(pos) + getSunlightHeatDelta(pos)

        if (!ThermodynamicsEngine.isUnchecked(savedHeat)) return savedHeat + deltaHeat

        return getBiomeHeat(pos) + deltaHeat
    }

    //用于应用方块修改后的热量影响
    override fun updateHeat(
        oldState: BlockState,
        newState: BlockState,
        pos: BlockPos
    ) {
        val oldStateHasHeat = ThermodynamicsEngine.hasHeat(oldState)
        val newStateHasHeat = ThermodynamicsEngine.hasHeat(newState)
        val oldStateHeat = ThermodynamicsEngine.getEpitaxialHeat(oldState, level, pos)
        val newStateHeat = ThermodynamicsEngine.getEpitaxialHeat(newState, level, pos)

        if (oldStateHasHeat) heatMaintainer.enqueueDecrease(pos, oldStateHeat)
        if (newStateHasHeat) heatMaintainer.enqueueIncrease(pos, newStateHeat)
        heatMaintainer.tryWakeUp()
    }

    override fun scanChunk(chunkPos: ChunkPos) {
        chunkScanner.enqueue(chunkPos)
        chunkScanner.tryWakeUp()
    }

    protected open fun getSunlightHeatDelta(pos: BlockPos): Float {
        if (level.getBiome(pos).`is`(Biomes.DESERT)) {
            return if (level.isDay) 10f else -55f
        }

        return if (level.isDay) 5f else 0f
    }

    open fun getBiomeHeat(pos: BlockPos): Float =
        ThermodynamicsEngine.Companion.fromFreezingPoint(BIOME_BASE_CELSIUS)

    protected open fun getWeatherHeatDelta(pos: BlockPos): Float = 0f

    @EventBusSubscriber(modid = Fallacy.Companion.MOD_ID)
    object Handler {
//        @SubscribeEvent
//        fun onChunkUnload(event: ChunkEvent.Unload) {
//            if (event.level.isClientSide) return
//
//            val chunk = event.chunk
//            val level = event.level as ServerLevel
//            val engine = getEngine(level)
//
//            if (engine !is EnvironmentThermodynamicsEngine) return
//
//            engine.unload(chunk.pos.toLong())
//        }

        @SubscribeEvent
        fun onPlayerTick(event: PlayerTickEvent.Post) {
            if (event.entity.level().isClientSide) return
            if (!TickHelper.checkServerTickRate(TickHelper.second(2))) return

            val player = event.entity as ServerPlayer
            val level = player.level() as ServerLevel
            val centerChunkPos = player.chunkPosition()
            val engine = getEngine(level)

            for (dx in -2..2) for (dz in -2..2) {
                val selectedChunkPos = ChunkPos(centerChunkPos.x + dx, centerChunkPos.z + dz)
                val selectedChunk = level.getChunk(selectedChunkPos.x, selectedChunkPos.z)

                if (getDistance(selectedChunkPos, centerChunkPos) > 2) continue
                if (selectedChunk.hasData(FallacyAttachments.CHUNK_HEAT)) continue
                engine.scanChunk(selectedChunkPos)
            }
        }

//        @SubscribeEvent
//        fun onChunkLoad(event: ChunkEvent.Load) {
//            if (event.level.isClientSide) return
//            if (event.chunk.hasData(FallacyAttachments.CHUNK_HEAT)) return
//
//            val level = event.level as ServerLevel
//            val chunk = event.chunk
//            val pos = chunk.pos
//
//            if (!shouldLoad(chunk, level)) return
//
//            getEngine(level).scanChunk(pos)
//        }

//        private fun shouldLoad(chunk: ChunkAccess, level: ServerLevel): Boolean =
//            level.players().any { p -> getDistance(p.chunkPosition(), chunk.pos) <= 2 }

        private fun getDistance(p1: ChunkPos, p2: ChunkPos): Int =
            (p1.x - p2.x).absoluteValue + (p1.z - p2.z).absoluteValue

        @SubscribeEvent
        fun onServerLevelTick(event: LevelTickEvent.Post) {
            if (event.level.isClientSide) return
            if (!TickHelper.checkServerTickRate(TickHelper.second(10))) return

            val level = event.level as ServerLevel
            val engine = getEngine(level)

            if (engine !is EnvironmentThermodynamicsEngine) return

            engine.heatMaintainer.tryWakeUp()
        }

        const val NBT_NAME = "fallacy_saved_heat_task"

        @SubscribeEvent
        fun onLevelLoad(event: LevelEvent.Load) {
            if (event.level.isClientSide) return
            val level = event.level as ServerLevel
            val data =
                level.dataStorage.get(SavedData.Factory(SavedHeatTask::create, SavedHeatTask::load), NBT_NAME) ?: return
            val engine = getEngine(level)

            if (engine !is EnvironmentThermodynamicsEngine) return
            engine.heatMaintainer.load(data.tasks)
        }

        @SubscribeEvent
        fun onLevelUnload(event: LevelEvent.Unload) {
            if (event.level.isClientSide) return
            val level = event.level as ServerLevel
//            level.dataStorage.computeIfAbsent(SavedData.Factory(SavedHeatTask::create, SavedHeatTask::load), NBT_NAME)
            val engine = getEngine(level)

            if (engine !is EnvironmentThermodynamicsEngine) return

            val saved = engine.heatMaintainer.getSaved()
            level.dataStorage.set(NBT_NAME, saved)
        }

        private val playerPosMap: MutableMap<UUID, BlockPos> = mutableMapOf()

        @SubscribeEvent
        fun debug(event: PlayerTickEvent.Post) {
            val player = event.entity as? ServerPlayer ?: return
            val level = player.level() as ServerLevel
            val uuid = player.uuid
            val pos = player.blockPosition()
            if (playerPosMap[uuid] == pos) return
            playerPosMap[uuid] = pos
            val heat = getEngine(level).getHeat(pos)
            val blockImpactHeat = ThermodynamicsEngine.initAndGetChunkHeat(pos, level).getHeatBy(pos, level)
            val temperature = Temperature.Kelvins(heat).toCelsius()
            player.sendSystemMessage(Component.literal("heat: $temperature, blockImpactHeat: $blockImpactHeat"))
        }
    }
}