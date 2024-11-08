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

class Sandstorm : Weather() {
    companion object {
        val ID = Fallacy.id("sandstorm")
    }

    override val namespaceId: ResourceLocation = ID

    override fun shouldTickEntities(level: ServerLevel, region: Region): Boolean = true

    override fun tickEntity(entity: LivingEntity, level: ServerLevel, pos: BlockPos) {
        if(!level.canSeeSky(pos)) return

        val effect = MobEffects.MOVEMENT_SLOWDOWN
        val effectInstance = MobEffectInstance(effect, TickHelper.second(5), 0)
        entity.addEffect(effectInstance)
    }
}