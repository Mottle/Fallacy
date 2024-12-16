package dev.deepslate.fallacy.thermodynamics.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.inject.item.FallacyThermodynamicsExtension
import dev.deepslate.fallacy.thermodynamics.ThermodynamicsEngine
import dev.deepslate.fallacy.thermodynamics.VanillaHeat
import dev.deepslate.fallacy.util.TickHelper
import dev.deepslate.fallacy.util.hasHeat
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.level.ChunkEvent
import net.neoforged.neoforge.event.tick.LevelTickEvent
import net.neoforged.neoforge.event.tick.PlayerTickEvent
import kotlin.math.absoluteValue
import kotlin.math.pow

open class EnvironmentThermodynamicsEngine(val level: Level) : ThermodynamicsEngine {

    companion object {
        const val BIOME_BASE_CELSIUS: Int = 15

        private val decayRate = Array(15) { i ->
            1f / (i.toFloat() * 1.2f.pow(i))
        }

        private fun getEngine(level: ServerLevel): ThermodynamicsEngine =
            (level as FallacyThermodynamicsExtension).`fallacy$getThermodynamicsEngine`()

        fun getEnvironmentEngineOrNull(level: ServerLevel): EnvironmentThermodynamicsEngine? =
            (level as? FallacyThermodynamicsExtension)?.`fallacy$getThermodynamicsEngine`() as? EnvironmentThermodynamicsEngine
    }

    override fun getHeat(pos: BlockPos): UInt {
        val savedHeat = level.getChunk(pos).getData(FallacyAttachments.CHUNK_HEAT)[level, pos]
        val deltaHeat = getWeatherHeatDelta(pos) + getSunlightHeatDelta(pos)

        if (!ThermodynamicsEngine.isUnchecked(savedHeat)) return (savedHeat.toInt() + deltaHeat).toUInt()

        return (getBiomeHeat(pos).toInt() + deltaHeat).toUInt()
    }

    private val chunkScanner = ChunkScanner(engine = this)

    private val heatCacheMaintainer = HeatCacheMaintainer(this)

    fun unload(packedChunkPos: Long) {
        chunkScanner.unload(packedChunkPos)
        heatCacheMaintainer.unload(packedChunkPos)
    }


    //用于应用方块修改后的热量影响
    override fun doCheck(pos: BlockPos) {
    }

    fun hasHeat(state: BlockState): Boolean = state.hasHeat() || VanillaHeat.hasHeat(state)

    fun hasHeat(pos: BlockPos): Boolean = level.getBlockState(pos).let(::hasHeat)

    protected open fun getSunlightHeatDelta(pos: BlockPos): Int {
        if (level.getBiome(pos).`is`(Biomes.DESERT)) {
            return if (level.isDay) 10 else -55
        }

        return if (level.isDay) 5 else 0
    }

    open fun getBiomeHeat(pos: BlockPos): UInt =
        ThermodynamicsEngine.Companion.fromFreezingPoint(BIOME_BASE_CELSIUS)

    protected open fun getWeatherHeatDelta(pos: BlockPos): Int = 0

    @EventBusSubscriber(modid = Fallacy.Companion.MOD_ID)
    object Handler {

        @SubscribeEvent
        fun onChunkUnload(event: ChunkEvent.Unload) {
            if (event.level.isClientSide) return

            val chunk = event.chunk
            val level = event.level as ServerLevel
            val engine = getEngine(level)

            if (engine !is EnvironmentThermodynamicsEngine) return

            engine.unload(chunk.getPos().toLong())
        }

        //修改加载策略为加载玩家附近的区块
        @SubscribeEvent
        fun onPlayerTick(event: PlayerTickEvent.Post) {
            val player = event.entity as? ServerPlayer ?: return

            if (!TickHelper.checkServerTickRate(TickHelper.second(2))) return

            val centerChunkPos = ChunkPos(player.blockPosition())
            val engine = getEngine(player.level() as ServerLevel)

            if (engine !is EnvironmentThermodynamicsEngine) return

            for (dx in -2..2) for (dz in -2..2) {
                val aroundChunkPos = ChunkPos(centerChunkPos.x + dx, centerChunkPos.z + dz)

                if (getDistance(aroundChunkPos, centerChunkPos) > 2) continue
                if (!engine.chunkScanner.hasScanned(aroundChunkPos)) {
                    engine.scannedSet.add(aroundChunkPos.toLong())
                    engine.scanChunk(aroundChunkPos)
                }
            }
            engine.scanChunks()
        }

        private fun getDistance(p1: ChunkPos, p2: ChunkPos): Int =
            (p1.x - p2.x).absoluteValue + (p1.z - p2.z).absoluteValue

        @SubscribeEvent
        fun onServerLevelTick(event: LevelTickEvent.Post) {
            if (event.level.isClientSide) return
            if (!TickHelper.checkServerTickRate(TickHelper.second(10))) return

            val level = event.level as ServerLevel
            val engine = getEngine(level)

            if (engine !is EnvironmentThermodynamicsEngine) return

            engine.scanChunks()
//            engine.partiallyScan()
            engine.runHeatUpdates()
        }
    }
}