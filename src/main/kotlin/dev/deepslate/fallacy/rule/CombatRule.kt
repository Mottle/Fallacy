package dev.deepslate.fallacy.rule

import dev.deepslate.fallacy.util.MathHelper
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.LivingEntity
import kotlin.math.absoluteValue

object CombatRule {
    const val K = 2.0

    const val T = 20

    fun getDamageAfterAbsorb(entity: LivingEntity, damage: Float, source: DamageSource, armor: Float): Float {
        if (armor.absoluteValue <= 0.1f) return damage
        if (armor > 0.1f) {
            val rate = MathHelper.nigmoid((damage + T) / (K * armor)).toFloat() * damage / (damage + armor)
            val finalDamage = rate * damage
            return finalDamage
        }

        val rate = armor.absoluteValue * 0.05f
        return damage * (1 + rate)
    }
}