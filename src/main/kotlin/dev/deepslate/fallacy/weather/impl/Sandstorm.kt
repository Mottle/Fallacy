package dev.deepslate.fallacy.weather.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.util.TickHelper
import dev.deepslate.fallacy.util.region.Region
import dev.deepslate.fallacy.weather.Weather
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biomes

class Sandstorm : Weather() {
    companion object {
        val ID = Fallacy.withID("sandstorm")
    }

    override val namespaceId: ResourceLocation = ID

    override fun shouldTickEntities(level: ServerLevel, region: Region): Boolean = true

    override fun tickEntity(entity: LivingEntity, level: ServerLevel, pos: BlockPos) {
        if (!level.canSeeSky(pos)) return
        if (entity.isInWater) return
        if (entity is Player && entity.isInvulnerable) return

        val effect = MobEffects.MOVEMENT_SLOWDOWN
        val effectInstance = MobEffectInstance(effect, TickHelper.second(12), 1)
        entity.addEffect(effectInstance)
    }

    override fun isValidAt(level: Level, pos: BlockPos): Boolean {
        val biome = level.getBiome(pos)
        return biome.`is`(Biomes.DESERT) || biome.`is`(Biomes.BADLANDS)
                || biome.`is`(Biomes.WOODED_BADLANDS) || biome.`is`(Biomes.ERODED_BADLANDS)
    }
}