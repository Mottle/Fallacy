package dev.deepslate.fallacy.common.item

import dev.deepslate.fallacy.common.item.data.ExtendedProperties
import dev.deepslate.fallacy.util.extension.internalExtendedProperties
import net.minecraft.world.item.Item

class FallacyItem(properties: Properties, extendedProperties: ExtendedProperties) : Item(properties) {
    init {
        this.internalExtendedProperties = extendedProperties
    }

    val extendedProperties = internalExtendedProperties
}