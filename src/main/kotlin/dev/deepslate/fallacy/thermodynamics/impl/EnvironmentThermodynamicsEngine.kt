package dev.deepslate.fallacy.thermodynamics.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.common.datapack.BiomesConfiguration
import dev.deepslate.fallacy.common.datapack.DataPack
import dev.deepslate.fallacy.inject.FallacyThermodynamicsExtension
import dev.deepslate.fallacy.thermodynamics.HeatStorageCache
import dev.deepslate.fallacy.thermodynamics.ThermodynamicsEngine
import dev.deepslate.fallacy.thermodynamics.data.HeatProcessQueue
import dev.deepslate.fallacy.thermodynamics.data.HeatStorage
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.thread.ProcessorMailbox
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biomes
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.level.ChunkEvent
import java.lang.ref.WeakReference
import java.util.concurrent.Executors

open class EnvironmentThermodynamicsEngine(override val level: Level) : ThermodynamicsEngine(), HeatStorageCache {

    companion object {
        fun getEnvironmentEngineOrNull(level: ServerLevel): EnvironmentThermodynamicsEngine? =
            (level as? FallacyThermodynamicsExtension)?.`fallacy$getThermodynamicsEngine`() as? EnvironmentThermodynamicsEngine
    }

    private val heatQueue: HeatProcessQueue = HeatProcessQueue()

    private val chunkScanner = ChunkScanner(3, this, heatQueue)

    private val positiveHeatCache: Long2ObjectOpenHashMap<WeakReference<HeatStorage>> = Long2ObjectOpenHashMap()

    private val negativeHeatCache: Long2ObjectOpenHashMap<WeakReference<HeatStorage>> = Long2ObjectOpenHashMap()

    override val cache: HeatStorageCache = this

    val scanTaskCount: Int
        get() = chunkScanner.taskCount

    val maintainTaskCount: Int
        get() = mailbox.size()

    val heatQueueSize: Int
        get() = heatQueue.size

    override fun queryPositive(chunkPos: ChunkPos): HeatStorage {
        val packed = chunkPos.toLong()
        val data = positiveHeatCache[packed]?.get()

        if (data == null) {
            return level.getChunk(chunkPos.x, chunkPos.z).getData(FallacyAttachments.POSITIVE_CHUNK_HEAT)
        }

        return data
    }

    override fun queryNegative(chunkPos: ChunkPos): HeatStorage {
        val packed = chunkPos.toLong()
        val data = negativeHeatCache[packed]?.get()

        if (data == null) {
            return level.getChunk(chunkPos.x, chunkPos.z).getData(FallacyAttachments.NEGATIVE_CHUNK_HEAT)
        }

        return data
    }

    private var biomeHeatCache: BiomesConfiguration? = null

    fun getBiomeHeat(pos: BlockPos): Int {
        if (biomeHeatCache == null) biomeHeatCache = DataPack.biomes(level)
        val biomeKey = level.getBiome(pos).key?.location() ?: return BiomesConfiguration.DEFAULT.heat
        return biomeHeatCache!!.query(biomeKey).heat
    }

    override fun getHeat(pos: BlockPos): Int {
        val packedChunkPos = ChunkPos.asLong(pos)
        val index = (pos.y - level.minBuildHeight) / 16

        val positiveHeatStorage = queryPositive(ChunkPos(packedChunkPos))
        val negativeHeatStorage = queryNegative(ChunkPos(packedChunkPos))

        val positiveHeat = positiveHeatStorage[index]?.getReadable(pos.x, pos.y, pos.z) ?: MIN_HEAT
        val negativeHeat = negativeHeatStorage[index]?.getReadable(pos.x, pos.y, pos.z) ?: MAX_HEAT
        val biomeHeat = getBiomeHeat(pos)
        val sunlightHeat = getSunlightHeatDelta(pos)
        val weatherHeat = getWeatherHeatDelta(pos)
        val dayNightCycleHeat = getDayNightCycleHeatDelta(pos)
        val environmentHeat = biomeHeat + sunlightHeat + weatherHeat + dayNightCycleHeat

        val positiveImpact = if (positiveHeat > environmentHeat) positiveHeat - environmentHeat else 0
        val negativeImpact = if (negativeHeat < environmentHeat) negativeHeat - environmentHeat else 0

        val finalHeat = environmentHeat + positiveImpact + negativeImpact

        return finalHeat
    }


    protected open fun getSunlightHeatDelta(pos: BlockPos): Int {
        if (level.isNight) return 0
        if (!level.canSeeSky(pos)) return 0

        val biome = level.getBiome(pos)

        if (biome.`is`(Biomes.DESERT)) return 25
        if (biome.`is`(Biomes.BADLANDS) || biome.`is`(Biomes.ERODED_BADLANDS) || biome.`is`(Biomes.WOODED_BADLANDS)) return 15

        return 5
    }

    protected open fun getDayNightCycleHeatDelta(pos: BlockPos): Int {
        if (level.getBiome(pos).`is`(Biomes.DESERT)) {
            return if (level.isDay) 0 else -55
        }

        return if (level.isDay) 0 else -3
    }

    protected open fun getWeatherHeatDelta(pos: BlockPos): Int = 0

    override fun checkBlock(pos: BlockPos) {
        if (pos.y !in level.minBuildHeight..level.maxBuildHeight) return
        heatQueue.enqueueBlockChange(pos)
    }

    override fun scanChunk(chunkPos: ChunkPos) {
        val chunk = level.getChunk(chunkPos.x, chunkPos.z)
        chunkScanner.enqueue(chunk)
    }

    override fun runUpdates() {
        propagateChanges()
    }

    @EventBusSubscriber(modid = Fallacy.Companion.MOD_ID)
    object Handler {

        @SubscribeEvent
        fun onChunkLoad(event: ChunkEvent.Load) {
            if (event.level.isClientSide) return

            val level = event.level as ServerLevel
            val chunk = event.chunk
            val packed = chunk.pos.toLong()
            val positiveData = chunk.getData(FallacyAttachments.POSITIVE_CHUNK_HEAT)
            val negativeData = chunk.getData(FallacyAttachments.NEGATIVE_CHUNK_HEAT)
            val engine = getEnvironmentEngineOrNull(level)

            engine?.positiveHeatCache[packed] = WeakReference(positiveData)
            engine?.negativeHeatCache[packed] = WeakReference(negativeData)
        }

        @SubscribeEvent
        fun onChunkUnload(event: ChunkEvent.Unload) {
            if (event.level.isClientSide) return

            val level = event.level as ServerLevel
            val chunk = event.chunk
            val packed = chunk.pos.toLong()
            val engine = getEnvironmentEngineOrNull(level)

            engine?.positiveHeatCache?.remove(packed)
            engine?.negativeHeatCache?.remove(packed)
        }

//        private val playerPosMap: MutableMap<UUID, BlockPos> = mutableMapOf()
//
//        @SubscribeEvent
//        fun debug(event: PlayerTickEvent.Post) {
//            val player = event.entity as? ServerPlayer ?: return
//            val uuid = player.uuid
//            val pos = player.blockPosition()
//            if (playerPosMap[uuid] == pos) return
//            playerPosMap[uuid] = pos
//            val heat = getHeatAt(player)
//            val celsius = Temperature.celsius(heat)
//            player.sendSystemMessage(Component.literal("heat: $celsius"))
//        }
    }

    private val mailbox = ProcessorMailbox.create(Executors.newFixedThreadPool(3), "fallacy-thermodynamics-process")

    fun propagateChanges() {
        if (heatQueue.empty) return
        if (mailbox.size() > 200) return

        while (!heatQueue.empty) {
            val task = heatQueue.dequeue() ?: break
            val positions = task.changedPosition
            if (positions.isNotEmpty()) {
                mailbox.tell {
                    PositiveHeatMaintainer(this).processHeatChanges(task.chunkPos, task.changedPosition)
                    NegativeHeatMaintainer(this).processHeatChanges(task.chunkPos, task.changedPosition)
                }
            }
        }
    }
}