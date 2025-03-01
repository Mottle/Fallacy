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

class Snowstorm : Weather() {
    companion object {
        val ID = Fallacy.withID("snowstorm")
    }

    override val namespaceId: ResourceLocation = ID

    override fun shouldTickEntities(level: ServerLevel, region: Region): Boolean = true

    override fun tickEntity(entity: LivingEntity, level: ServerLevel, pos: BlockPos) {
        if (!level.canSeeSky(pos)) return
        if (entity.isInWater) return
        if (entity is Player && entity.isInvulnerable) return

        if (entity is Player) {
            entity.ticksFrozen = 300
        }

        val effect = MobEffects.MOVEMENT_SLOWDOWN
        val effectInstance = MobEffectInstance(effect, TickHelper.second(12), 1)
        entity.addEffect(effectInstance)
    }

    override fun isValidAt(level: Level, pos: BlockPos): Boolean {
        val biome = level.getBiome(pos)
        return biome.value().coldEnoughToSnow(pos)
    }
}