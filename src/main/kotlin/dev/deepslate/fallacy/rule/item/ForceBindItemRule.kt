package dev.deepslate.fallacy.rule.item

import dev.deepslate.fallacy.common.item.data.FallacyDataComponents
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ItemStack

//玩家无法脱下存在FORCE_BINDING的物品
object ForceBindItemRule {
    fun check(slot: EquipmentSlot, itemStack: ItemStack): Boolean {
        if (slot !in SLOTS) return false
        return itemStack.has(FallacyDataComponents.FORCE_BINDING)
    }

    val SLOTS = listOf(EquipmentSlot.FEET, EquipmentSlot.LEGS, EquipmentSlot.CHEST, EquipmentSlot.HEAD)
}