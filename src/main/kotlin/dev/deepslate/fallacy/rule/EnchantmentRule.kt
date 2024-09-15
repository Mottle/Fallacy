package dev.deepslate.fallacy.rule

import dev.deepslate.fallacy.race.Race
import dev.deepslate.fallacy.race.Undead
import net.minecraft.core.Holder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import org.apache.commons.lang3.mutable.MutableFloat

object EnchantmentRule {
    fun modifyDamage(amount: MutableFloat, holder: Holder<Enchantment>, enchantmentLevel: Int, target: Entity) {
        modifierMap.filter { it.check(holder, enchantmentLevel, target) }
            .map { it.modify(amount, holder, enchantmentLevel, target) }
    }

    interface DamageModifier {
        fun check(holder: Holder<Enchantment>, enchantmentLevel: Int, target: Entity): Boolean

        fun modify(amount: MutableFloat, holder: Holder<Enchantment>, enchantmentLevel: Int, target: Entity)
    }

    private val smite_modifier = object : DamageModifier {
        override fun check(holder: Holder<Enchantment>, enchantmentLevel: Int, target: Entity): Boolean {
            return holder.key == Enchantments.SMITE && target is Player && Race.get(target) is Undead
        }

        override fun modify(amount: MutableFloat, holder: Holder<Enchantment>, enchantmentLevel: Int, target: Entity) {
            amount.add(enchantmentLevel * 2.5)
        }
    }

    private val modifierMap: Set<DamageModifier> = setOf(smite_modifier)
}