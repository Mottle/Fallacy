package dev.deepslate.fallacy.common.item

import dev.deepslate.fallacy.common.item.data.ExtendedPropertiesLike
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier

open class FallacyTieredItem(val tier: Tier, properties: Properties, extendedProperties: ExtendedPropertiesLike) :
    FallacyItem(properties.durability(tier.uses), extendedProperties) {

    @Deprecated("Deprecated in Java")
    override fun getEnchantmentValue(): Int = tier.enchantmentValue

    override fun isValidRepairItem(stack: ItemStack, repairCandidate: ItemStack): Boolean =
        tier.repairIngredient.test(stack) || super.isValidRepairItem(stack, repairCandidate)
}