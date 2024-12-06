package dev.deepslate.fallacy.thermodynamics

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.inject.item.FallacyThermodynamicsExtension
import dev.deepslate.fallacy.thermodynamics.data.ChunkHeatTarget
import dev.deepslate.fallacy.util.TickHelper
import dev.deepslate.fallacy.util.getEpitaxialHeat
import dev.deepslate.fallacy.util.hasHeat
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.thread.ProcessorMailbox
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.block.LiquidBlock
import net.minecraft.world.level.chunk.ChunkAccess
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.level.ChunkEvent
import net.neoforged.neoforge.event.tick.LevelTickEvent
import net.neoforged.neoforge.event.tick.PlayerTickEvent
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import kotlin.math.absoluteValue
import kotlin.math.pow

open class EnvironmentThermodynamicsEngine(private val level: Level) : ThermodynamicsEngine {

    companion object {
        const val BIOME_BASE_CELSIUS: Int = 15

        private val decayRate = Array(15) { i ->
            1f / (i.toFloat() * 1.2f.pow(i))
        }
    }

    override fun getHeat(pos: BlockPos): UInt =
        (level.getChunk(pos).getData(FallacyAttachments.CHUNK_HEAT)[level, pos].toInt()
                + getWeatherHeatDelta(pos) + getSunlightHeatDelta(pos)).toUInt()

    //用于应用方块修改后的热量影响
    override fun doCheck(pos: BlockPos) {
        addUpdateTask {
            val state = level.getBlockState(pos)
            if (state.hasHeat()) {
                checkSet.add(pos.asLong())
            } else if (VanillaHeat.hasHeat(state)) {
                checkSet.add(pos.asLong())
            }
        }
    }

    private fun doCheck(poses: List<Long>) {
        addUpdateTask {
            poses.forEach { packedPos ->
                val pos = BlockPos.of(packedPos)
                val state = level.getBlockState(pos)
                if (state.hasHeat()) {
                    checkSet.add(packedPos)
                } else if (VanillaHeat.hasHeat(state)) {
                    checkSet.add(packedPos)
                }
            }
        }
    }

    protected open fun getSunlightHeatDelta(pos: BlockPos): Int {
        if (level.getBiome(pos).`is`(Biomes.DESERT)) {
            return if (level.isDay) 10 else -55
        }

        return if (level.isDay) 5 else 0
    }

    protected open fun getBiomeHeat(pos: BlockPos): UInt = ThermodynamicsEngine.fromFreezingPoint(BIOME_BASE_CELSIUS)

    protected open fun getWeatherHeatDelta(pos: BlockPos): Int = 0

    protected open val dimensionHeat: Int = 0

    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object Handler {
//        @SubscribeEvent
//        fun onChunkLoad(event: ChunkEvent.Load) {
//            if (event.level.isClientSide) return
//
//            val chunk = event.chunk
//            val level = event.level as ServerLevel
//            val engine = getEngine(level)
//
//            if (engine !is EnvironmentThermodynamicsEngine) return
//
////            load(engine, chunk)
//            engine.scanChunk(chunk.pos)
//        }

        @SubscribeEvent
        fun onChunkUnload(event: ChunkEvent.Unload) {
            if (event.level.isClientSide) return

            val chunk = event.chunk
            val level = event.level as ServerLevel
            val engine = getEngine(level)

            if (engine !is EnvironmentThermodynamicsEngine) return

            unload(engine, chunk)
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
                if (!engine.chunkThermodynamicsMap.contains(aroundChunkPos.toLong())
                    || !engine.chunkThermodynamicsMap[aroundChunkPos.toLong()]!!.loadFinished.get()
                ) {
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
            engine.runHeatUpdates()
        }

        private fun getEngine(level: ServerLevel): ThermodynamicsEngine =
            (level as FallacyThermodynamicsExtension).`fallacy$getThermodynamicsEngine`()

//        private fun load(engine: EnvironmentThermodynamicsEngine, chunk: ChunkAccess) {
//            engine.addScanTask {
//                engine.chunkThermodynamicsMap[chunk.pos.toLong()] = ChunkHeatTarget(chunk)
//                Fallacy.LOGGER.debug("Loaded chunk {} to thermodynamics engine", chunk.pos)
////                Fallacy.LOGGER.debug("Map size: ${engine.chunkThermodynamicsMap.size}")
//            }
//        }

        private fun unload(engine: EnvironmentThermodynamicsEngine, chunk: ChunkAccess) {
            engine.addScanTask {
                engine.chunkThermodynamicsMap.remove(chunk.pos.toLong())
            }
        }
    }

    fun scanChunk(chunkPos: ChunkPos) {
        addScanTask {
            scanSet.add(chunkPos.toLong())
        }
    }

    private fun scanChunks() {
        addScanTask {

            val copiedSet = synchronized(scanSet) {
                val copiedSet = scanSet.clone()
                scanSet.clear()
                scanSet.trim(512)
                copiedSet
            }

            val iterator = copiedSet.iterator()
            val checkPackedPoses = LongOpenHashSet()

            while (iterator.hasNext()) {
                val packedChunkPos = iterator.nextLong()
                val chunkPos = ChunkPos(packedChunkPos)
                if (chunkThermodynamicsMap[packedChunkPos]?.loadFinished?.get() == true) return@addScanTask

                val chunkTarget = ChunkHeatTarget(level)
                val yRange = with(level) { minBuildHeight..maxBuildHeight }
                val xRange = (0..15)
                val zRange = (0..15)

                for (x in xRange) for (y in yRange) for (z in zRange) {
                    val localBlockPos = BlockPos(x, y, z)
                    val fullBlockPos = BlockPos(chunkPos.minBlockX + x, y, chunkPos.minBlockZ + z)

                    applyBaseHeat(localBlockPos, chunkTarget)
                    checkPackedPoses.add(fullBlockPos.asLong())
                }

                chunkTarget.loadFinished.set(true)
                chunkThermodynamicsMap[packedChunkPos] = chunkTarget
                Fallacy.LOGGER.info("chunk ${ChunkPos(packedChunkPos)} scan finished.")
            }
            doCheck(checkPackedPoses.toList())
        }
    }

    private fun applyBaseHeat(pos: BlockPos, target: ChunkHeatTarget) {
        val baseHeat = getBiomeHeat(pos).toUShort()
        target[pos] = ChunkHeatTarget.HeatCounter(baseHeat, 1u)
    }

    private fun runHeatUpdates() {
        if (checkSet.isEmpty()) return
        addUpdateTask {
            val iterator = checkSet.iterator()
            val relativeChunks = LongOpenHashSet()
            val waitTask = LongOpenHashSet()

            var count = 1
            while (iterator.hasNext()) {
                val packedPos = iterator.nextLong()
                val pos = BlockPos.of(packedPos)

                if (!checkChunkLoaded(pos)) {
                    waitTask.add(packedPos)
                    continue
                }

                update(packedPos)
                relativeChunks.add(ChunkPos.asLong(pos))
                if (count % 100 == 0) Fallacy.LOGGER.info("heat update: $count/${checkSet.size}.")
                ++count
            }

            checkSet.clear()
            checkSet.trim(512)
            checkSet.addAll(waitTask)

            relativeChunks.forEach { packedPos ->
                applyHeatTarget(packedPos)
                Fallacy.LOGGER.info("apply chunk {}", ChunkPos(packedPos))
            }
        }
    }

    private fun checkChunkLoaded(pos: BlockPos): Boolean {
        val centerChunkPos = ChunkPos(pos)
        for (dx in -1..1) for (dz in -1..1) {
            val aroundChunkPos = ChunkPos(centerChunkPos.x + dx, centerChunkPos.z + dz)
            val packedAroundChunkPos = aroundChunkPos.toLong()
            val data = chunkThermodynamicsMap[packedAroundChunkPos]
            if (data == null) {
                scanChunk(aroundChunkPos)
                return false
            }
            if (!data.loadFinished.get()) return false
        }

        return true
    }

    private fun update(packedPos: Long) {
        val usedSet = LongOpenHashSet(1024)
        val queue = LongArrayFIFOQueue(1024)
        val startPos = BlockPos.of(packedPos)
        val state = level.getBlockState(startPos)

        if (!state.hasHeat() && !VanillaHeat.hasHeat(state)) return

        val startHeat = getExHeat(startPos)

        queue.enqueue(packedPos)
        queue.enqueue(startHeat.toLong())
        usedSet.add(packedPos)
        while (!queue.isEmpty) {
            val packed = queue.dequeueLong()
            val pos = BlockPos.of(packed)
            val posHeat = queue.dequeueLong().toUInt()
            Direction.entries.forEach { direction ->
                val nextPos = pos.relative(direction)
                val packedNextPos = nextPos.asLong()
                val dis = nextPos.distManhattan(startPos)
                val biomeHeat = getBiomeHeat(nextPos)

                if (packedNextPos in usedSet) return@forEach
                if (dis > 15) return@forEach

                val nextHeat = (decayHeat(posHeat, biomeHeat, dis) * getThermalConductivity(nextPos)).toUInt()
                if ((nextHeat.toInt() - startHeat.toInt()).absoluteValue < 5) return@forEach

                updateHeatTarget(nextPos, nextHeat)
                queue.enqueue(packedNextPos)
                queue.enqueue(nextHeat.toLong())
                usedSet.add(packedNextPos)
            }
        }
    }

    private fun getExHeat(pos: BlockPos): UInt {
        val state = level.getBlockState(pos)
        if (state.hasHeat()) return state.getEpitaxialHeat(level, pos)
        return VanillaHeat.getHeat(state)
    }

    private fun decayHeat(heat: UInt, envHeat: UInt, dis: Int): Float {
        val rate = 1f / (dis.toFloat() * 1.2f.pow(dis - 1))
        return heat.toFloat() * rate + envHeat.toFloat() * (1f - rate)
    }

    private fun getThermalConductivity(pos: BlockPos): Float {
        val state = level.getBlockState(pos)
        if (state.isAir) return 1f
        if (state.block is LiquidBlock) return 0.9f
        if (state.isCollisionShapeFullBlock(level, pos)) return 0.4f
        return 0.75f
    }

    private fun updateHeatTarget(pos: BlockPos, heat: UInt) {
        val packedChunkPos = ChunkPos.asLong(pos)

        val target = chunkThermodynamicsMap[packedChunkPos] ?: return
        val heatCounter = target[pos]
        val newHeat =
            ((heatCounter.heat.toFloat() * heatCounter.sourceCount.toFloat() + heat.toFloat()) / (heatCounter.sourceCount.toFloat() + 1f)).toUInt()
        val newHeatCounter = ChunkHeatTarget.HeatCounter(newHeat.toUShort(), (heatCounter.sourceCount + 1u).toUShort())

        target[pos] = newHeatCounter
    }

    private fun applyHeatTarget(packedChunkPos: Long) {
        val chunkPos = ChunkPos(packedChunkPos)
        val target = chunkThermodynamicsMap[packedChunkPos] ?: return
        val layerStack = target.toLayerStack()
        val chunk = level.getChunk(chunkPos.x, chunkPos.z)

        chunk.setData(FallacyAttachments.CHUNK_HEAT, layerStack)
        chunk.isUnsaved = true
    }

    //threaded!!!
    private val chunkThermodynamicsMap: ConcurrentHashMap<Long, ChunkHeatTarget> = ConcurrentHashMap(1024)

    //scan chunk
    private val scanSet: LongOpenHashSet = LongOpenHashSet(512, 0.5f)

    //block to check, only on update thread
    private val checkSet: LongOpenHashSet = LongOpenHashSet(512, 0.5f)

    private val updateMailBox =
        ProcessorMailbox.create(Executors.newSingleThreadExecutor(), "fallacy-thermodynamics-update")

    private val chunkScanMailbox =
        ProcessorMailbox.create(Executors.newFixedThreadPool(2), "fallacy-thermodynamics-scan")

    fun addScanTask(task: Runnable) {
        chunkScanMailbox.tell(task)
    }

    fun addUpdateTask(task: Runnable) {
        updateMailBox.tell(task)
    }
}