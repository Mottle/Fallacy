package dev.deepslate.fallacy.behavior.impl

import dev.deepslate.fallacy.behavior.TickableBehavior
import dev.deepslate.fallacy.common.data.BehaviorTags
import dev.deepslate.fallacy.util.announce.Autoload
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects

@Autoload
class WeaknessInDay : TickableBehavior {

    private fun createWeakness() = MobEffectInstance(MobEffects.WEAKNESS, 20 * 10, 0)

    override val interval: Int = 8 * 20

    override fun tick(
        level: ServerLevel,
        player: ServerPlayer,
        position: BlockPos
    ) {
        if (!level.isDay) return
        player.addEffect(createWeakness())
    }

    override val tagRequired: List<ResourceLocation> = listOf(BehaviorTags.WEAKNESS_IN_DAY)
}