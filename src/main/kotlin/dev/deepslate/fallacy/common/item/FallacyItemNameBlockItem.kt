package dev.deepslate.fallacy.common.item

import dev.deepslate.fallacy.common.item.data.ExtendedProperties
import net.minecraft.core.Holder
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import java.util.*

class FallacyItemNameBlockItem(holder: Holder<Block>, properties: Properties, extendedProperties: ExtendedProperties) :
    FallacyBlockItem(holder.value(), properties, extendedProperties) {

    override fun getDescriptionId(): String = orCreateDescriptionId

    override fun getTooltipImage(stack: ItemStack): Optional<TooltipComponent> {
        return super.getTooltipImage(stack)
    }
}