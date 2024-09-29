package dev.deepslate.fallacy.rule.item

import dev.deepslate.fallacy.behavior.BehaviorTags
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import org.apache.commons.lang3.mutable.MutableFloat

object SmiteEnchantmentRule {
    fun modifyDamage(amount: MutableFloat, holder: Holder<Enchantment>, enchantmentLevel: Int, target: Entity) {
        modifierMap.filter { it.check(holder, enchantmentLevel, target) }
            .map { it.modify(amount, holder, enchantmentLevel, target) }
    }

    interface DamageModifier {
        fun check(holder: Holder<Enchantment>, enchantmentLevel: Int, target: Entity): Boolean

        fun modify(amount: MutableFloat, holder: Holder<Enchantment>, enchantmentLevel: Int, target: Entity)
    }

    private val smiteModifier = object : DamageModifier {
        override fun check(holder: Holder<Enchantment>, enchantmentLevel: Int, target: Entity): Boolean {
            if (target !is ServerPlayer) return false
            return holder.key == Enchantments.SMITE && BehaviorTags.has(target, BehaviorTags.UNDEAD)
        }

        override fun modify(amount: MutableFloat, holder: Holder<Enchantment>, enchantmentLevel: Int, target: Entity) {
            amount.add(enchantmentLevel * 2.5)
        }
    }

    private val modifierMap: Set<DamageModifier> = setOf(smiteModifier)
}