package dev.deepslate.fallacy.common.effect

import dev.deepslate.fallacy.common.capability.FallacyCapabilities
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity

class Dehydration : MobEffect(MobEffectCategory.HARMFUL, 0xffffff) {
    override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int): Boolean = duration % 20 == 0

    override fun applyEffectTick(
        livingEntity: LivingEntity,
        amplifier: Int
    ): Boolean {
        val cap = livingEntity.getCapability(FallacyCapabilities.THIRST) ?: return false

        //口渴至大于0f则消失
        if (cap.value > 0f) return false
        return true
    }
}