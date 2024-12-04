package dev.deepslate.fallacy.thermodynamics

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.inject.item.FallacyThermodynamicsExtension
import dev.deepslate.fallacy.thermodynamics.data.ChunkHeatTarget
import dev.deepslate.fallacy.thermodynamics.data.HeatLayer
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.thread.ProcessorMailbox
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.chunk.ChunkAccess
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.level.ChunkEvent
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors

open class EnvironmentThermodynamicsEngine(private val level: Level) : ThermodynamicsEngine {

    companion object {
        const val BIOME_BASE_CELSIUS: Int = 15
    }

    override fun getEpitaxialHeat(pos: BlockPos): UInt = HeatLayer.FREEZING_POINT

    override fun getIntrinsicHeat(pos: BlockPos): UInt =
        ThermodynamicsEngine.fromFreezingPoint(getBiomeHeat(pos) + getSunlightHeat(pos) + getWeatherHeat(pos) + dimensionHeat)

    override fun doCheck(pos: BlockPos) {
        /* DO NOTHING */
    }

    protected open fun getSunlightHeat(pos: BlockPos): Int {
        if (level.getBiome(pos).`is`(Biomes.DESERT)) {
            return if (level.isDay) 10 else -55
        }

        return if (level.isDay) 5 else 0
    }

    protected open fun getBiomeHeat(pos: BlockPos): Int = (BIOME_BASE_CELSIUS * level.getBiome(pos)
        .value().baseTemperature).toInt()

    protected open fun getWeatherHeat(pos: BlockPos): Int = 0

    protected open val dimensionHeat: Int = 0

    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object Handler {
        @SubscribeEvent
        fun onChunkLoad(event: ChunkEvent.Load) {
            if (event.level.isClientSide) return
            val chunk = event.chunk
            val level = event.level as ServerLevel
            val engine = getEngine(level)
            load(engine, chunk)
        }

        @SubscribeEvent
        fun onChunkUnload(event: ChunkEvent.Unload) {
            if (event.level.isClientSide) return
            val chunk = event.chunk
            val level = event.level as ServerLevel
            val engine = getEngine(level)
            unload(engine, chunk)
        }

        private fun getEngine(level: ServerLevel): ThermodynamicsEngine =
            (level as FallacyThermodynamicsExtension).`fallacy$getThermodynamicsEngine`()

        private fun load(engine: ThermodynamicsEngine, chunk: ChunkAccess) {
            if(engine !is EnvironmentThermodynamicsEngine) return
            engine.addTask {
                engine.chunkThermodynamicsMap[chunk.pos.toLong()] = ChunkHeatTarget(chunk)
            }
        }

        private fun unload(engine: ThermodynamicsEngine, chunk: ChunkAccess) {
            if(engine !is EnvironmentThermodynamicsEngine) return
            engine.addTask {
                engine.chunkThermodynamicsMap.remove(chunk.pos.toLong())
            }
        }
    }

    //threaded!!!
    private val chunkThermodynamicsMap: ConcurrentHashMap<Long, ChunkHeatTarget> = ConcurrentHashMap()

    private val mailbox = ProcessorMailbox.create(Executors.newSingleThreadExecutor(), "fallacy-thermodynamics")

    fun addTask(task: Runnable) {
        mailbox.tell(task)
    }
}