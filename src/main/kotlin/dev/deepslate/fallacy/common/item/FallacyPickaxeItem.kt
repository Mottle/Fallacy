package dev.deepslate.fallacy.common.item

import dev.deepslate.fallacy.common.item.data.ExtendedPropertiesLike
import net.minecraft.tags.BlockTags
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Tier
import net.neoforged.neoforge.common.ItemAbilities
import net.neoforged.neoforge.common.ItemAbility

class FallacyPickaxeItem(tier: Tier, properties: Properties, extendedProperties: ExtendedPropertiesLike) :
    FallacyDiggerItem(tier, BlockTags.MINEABLE_WITH_PICKAXE, properties, extendedProperties) {
    override fun canPerformAction(stack: ItemStack, itemAbility: ItemAbility): Boolean =
        ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(itemAbility)
}