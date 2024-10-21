package dev.deepslate.fallacy.weather.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.util.region.Region
import dev.deepslate.fallacy.weather.Weather
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.phys.Vec3

class Thunder : Weather() {

    companion object {
        val ID = Fallacy.id("thunder")
    }

    override fun shouldTickEntities(level: ServerLevel, region: Region): Boolean = true

    override fun tickEntity(entity: Entity, level: ServerLevel, pos: BlockPos) {

        val chunkPos = entity.chunkPosition()
        val randomPos = level.getBlockRandomPos(chunkPos.x, 0, chunkPos.z, 15)
        val pos = level.findLightningTargetAround(randomPos)
        if (level.isRainingAt(pos)) {
            val lightingBolt = EntityType.LIGHTNING_BOLT.create(level) ?: return
            lightingBolt.moveTo(Vec3.atBottomCenterOf(pos))
//            lightingBolt.setVisualOnly(flag1)
            level.addFreshEntity(lightingBolt)
        }
    }

    override val isWet: Boolean = true

    override val namespaceId: ResourceLocation = ID
}