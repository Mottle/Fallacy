package dev.deepslate.fallacy.util

import dev.deepslate.fallacy.common.item.data.ExtendedProperties
import dev.deepslate.fallacy.inject.item.FallacyExtendedItem
import net.minecraft.world.item.Item

var Item.extendedProperties: ExtendedProperties?
    get() = (this as? FallacyExtendedItem)?.`fallacy$getExtendedProperties`()
    set(value) {
        (this as? FallacyExtendedItem)?.`fallacy$setExtendedProperties`(value)
    }