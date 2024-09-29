package dev.deepslate.fallacy.rule

import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.ai.attributes.RangedAttribute

object RangedAttributeRule {
    fun rule() {
        with(Attributes.ARMOR.value() as RangedAttribute) {
            minValue = -1024.0
            maxValue = 1024.0
        }

        with(Attributes.ARMOR_TOUGHNESS.value() as RangedAttribute) {
            minValue = -1024.0
            maxValue = 1024.0
        }

        with(Attributes.ATTACK_DAMAGE.value() as RangedAttribute) {
            maxValue = Double.MAX_VALUE
        }

        with(Attributes.MAX_HEALTH.value() as RangedAttribute) {
            maxValue = 32784.0
        }
    }
}