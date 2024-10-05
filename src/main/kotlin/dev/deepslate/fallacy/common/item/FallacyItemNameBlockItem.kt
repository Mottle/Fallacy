package dev.deepslate.fallacy.common.item

import dev.deepslate.fallacy.common.item.data.ExtendedProperties
import net.minecraft.core.Holder
import net.minecraft.world.level.block.Block

class FallacyItemNameBlockItem(holder: Holder<Block>, properties: Properties, extendedProperties: ExtendedProperties) :
    FallacyBlockItem(holder.value(), properties, extendedProperties) {

    override fun getDescriptionId(): String = orCreateDescriptionId
}