package dev.deepslate.fallacy.weather.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.util.region.Region
import dev.deepslate.fallacy.weather.Weather
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.phys.Vec3

class Thunder : Weather() {

    companion object {
        val ID = Fallacy.id("thunder")
    }

    override fun shouldTickEntities(level: ServerLevel, region: Region): Boolean = true

    override fun tickEntity(entity: Entity, level: ServerLevel, pos: BlockPos) {
        trySpawnLightningBolt(level, entity.chunkPosition())
    }

    private fun trySpawnLightningBolt(level: ServerLevel, chunkPos: ChunkPos) {
        if (level.random.nextInt(20) != 1) return

        val count = level.random.nextInt(9) + 1

        for (i in 0 until count) {
            val randomX = chunkPos.x - level.random.nextIntBetweenInclusive(-15, 15)
            val randomZ = chunkPos.z - level.random.nextIntBetweenInclusive(-15, 15)
            val randomPos = level.getBlockRandomPos(randomX, 0, randomZ, 15)
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