package dev.deepslate.fallacy.weather.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.util.region.Region
import dev.deepslate.fallacy.weather.Weather
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.phys.Vec3

class Thunder : Weather() {

    companion object {
        val ID = Fallacy.withID("thunder")
    }

    override val priority: Int = 2

    override fun shouldTickEntities(level: ServerLevel, region: Region): Boolean = true

    override fun tickEntity(entity: LivingEntity, level: ServerLevel, pos: BlockPos) {
        if (entity is Player) trySpawnLightningBolt(level, entity.chunkPosition())
    }

    private fun trySpawnLightningBolt(level: ServerLevel, chunkPos: ChunkPos) {
        val random = level.random
        if (random.nextInt(40) != 1) return

        val count = random.nextInt(2) + 1
        val randomChunks = ChunkPos.rangeClosed(chunkPos, 3).toList()

        for (i in 0 until count) {
            if (i != 0 && random.nextInt(5) != 0) break

            val randomIndex = random.nextIntBetweenInclusive(0, randomChunks.size - 1)
            val randomChunkPos = randomChunks[randomIndex]
            val randomPos = level.getBlockRandomPos(randomChunkPos.minBlockX, 0, randomChunkPos.minBlockZ, 15)
            val pos = level.findLightningTargetAround(randomPos)

            if (level.isRainingAt(pos)) {
                val lightingBolt = EntityType.LIGHTNING_BOLT.create(level) ?: return
                lightingBolt.moveTo(Vec3.atBottomCenterOf(pos))
                level.addFreshEntity(lightingBolt)
            }
        }
    }

    override val isWet: Boolean = true

    override val namespaceId: ResourceLocation = ID
}