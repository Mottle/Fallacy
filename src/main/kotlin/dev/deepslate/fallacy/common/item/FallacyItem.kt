package dev.deepslate.fallacy.common.item

import dev.deepslate.fallacy.common.item.component.FallacyDataComponents
import dev.deepslate.fallacy.common.item.data.ExtendedPropertiesLike
import dev.deepslate.fallacy.util.extension.internalExtendedProperties
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

open class FallacyItem(properties: Properties, extendedProperties: ExtendedPropertiesLike) : Item(properties),
    ImmutableExtendedProperties {
    init {
        this.internalExtendedProperties = extendedProperties.get()
    }

    val extendedProperties
        get() = internalExtendedProperties!!

    fun rank(stack: ItemStack): Int {
        if (stack.has(FallacyDataComponents.RANK)) return stack.get(FallacyDataComponents.RANK)!!
        return extendedProperties.rank
    }

    fun isDeprecated(stack: ItemStack): Boolean {
        return stack.has(FallacyDataComponents.DEPRECATED) || extendedProperties.deprecated
    }
}