package dev.deepslate.fallacy.common.item

import dev.deepslate.fallacy.common.item.data.ExtendedPropertiesLike
import net.minecraft.core.component.DataComponents
import net.minecraft.tags.TagKey
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.minecraft.world.level.block.Block

open class FallacyDiggerItem(
    tier: Tier, blocks: TagKey<Block>, properties: Properties, extendedProperties: ExtendedPropertiesLike
) : FallacyTieredItem(
    tier, properties.component(DataComponents.TOOL, tier.createToolProperties(blocks)), extendedProperties
) {
    override fun hurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
        return true
    }

    override fun postHurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity) {
        stack.hurtAndBreak(2, attacker, EquipmentSlot.MAINHAND)
    }
}