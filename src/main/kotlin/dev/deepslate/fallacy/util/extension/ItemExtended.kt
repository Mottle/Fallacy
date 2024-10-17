package dev.deepslate.fallacy.util.extension

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.item.data.ExtendedProperties
import dev.deepslate.fallacy.inject.item.FallacyItemExtension
import net.minecraft.world.item.Item

internal var Item.internalExtendedProperties: ExtendedProperties?
    get() = (this as? FallacyItemExtension)?.`fallacy$getExtendedProperties`()
    set(value) {
        (this as? FallacyItemExtension)?.`fallacy$setExtendedProperties`(value) ?: {
            Fallacy.LOGGER.info("Item $this does not implement FallacyExtendedItem")
        }
    }

val Item.extendedProperties: ExtendedProperties?
    get() = internalExtendedProperties