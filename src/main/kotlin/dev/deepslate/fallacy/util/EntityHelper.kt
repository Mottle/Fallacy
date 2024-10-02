package dev.deepslate.fallacy.util

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

object EntityHelper {
    fun checkUndeadBurning(level: Level, entity: LivingEntity, position: BlockPos): Boolean {
        if (!level.isDay) return false
        if (!level.canSeeSky(position)) return false
        with(entity) {
            if (isInWaterRainOrBubble) return false
            if (isInPowderSnow) return false
            if (wasInPowderSnow) return false
        }
        return true
    }

    fun damageHead(entity: LivingEntity, damage: Int) {
        val head = entity.getItemBySlot(EquipmentSlot.HEAD)
        if (head.isEmpty) return

        head.damageValue += damage
        if (head.damageValue >= head.maxDamage) {
            entity.onEquippedItemBroken(head.item, EquipmentSlot.HEAD)
            entity.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY)
        }
    }
}