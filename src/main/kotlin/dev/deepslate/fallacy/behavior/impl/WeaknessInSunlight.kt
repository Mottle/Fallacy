package dev.deepslate.fallacy.behavior.impl

import dev.deepslate.fallacy.behavior.BehaviorTags
import dev.deepslate.fallacy.behavior.TickableBehavior
import dev.deepslate.fallacy.util.announce.Autoload
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EquipmentSlot

@Autoload
class WeaknessInSunlight : TickableBehavior {
    private fun createWeakness2() = MobEffectInstance(MobEffects.WEAKNESS, 20 * 5, 0)

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

    override val tagRequired: List<ResourceLocation> = listOf(BehaviorTags.WEAKNESS_IN_SUNLIGHT)
}