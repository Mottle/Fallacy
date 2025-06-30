package dev.deepslate.fallacy.trait.impl

import dev.deepslate.fallacy.trait.TickTrait
import dev.deepslate.fallacy.util.announce.Autoload
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects

@Autoload
class WeaknessInDay : TickTrait {

    private fun createWeakness() = MobEffectInstance(MobEffects.WEAKNESS, 20 * 10, 0)

    override val interval: Int = 8 * 20

    override fun tick(
        level: ServerLevel,
        player: ServerPlayer,
        position: BlockPos
    ) {
        if (!level.isDay) return
        if (player.isInvulnerable) return
        player.addEffect(createWeakness())
    }
}