package dev.deepslate.fallacy.trait.impl

import dev.deepslate.fallacy.trait.TickTrait
import dev.deepslate.fallacy.util.announce.Autoload
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EquipmentSlot

@Autoload
class Weakness2InSunlight : TickTrait {

    private fun createWeakness2() = MobEffectInstance(MobEffects.WEAKNESS, 20 * 5, 1)

    override val interval: Int
        get() = 4 * 20

    override fun tick(
        level: ServerLevel,
        player: ServerPlayer,
        position: BlockPos
    ) {
        if (!level.isDay) return
        if (!level.canSeeSky(position)) return
        if (!player.getItemBySlot(EquipmentSlot.HEAD).isEmpty) return
        if (player.isInvulnerable) return

        player.addEffect(createWeakness2())
    }
}