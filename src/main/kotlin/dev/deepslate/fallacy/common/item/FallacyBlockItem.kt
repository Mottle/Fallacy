package dev.deepslate.fallacy.common.item

import dev.deepslate.fallacy.common.item.data.ExtendedProperties
import dev.deepslate.fallacy.util.extension.internalExtendedProperties
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

open class FallacyBlockItem(block: Block, properties: Item.Properties, extendedProperties: ExtendedProperties) :
    BlockItem(block, properties) {
    init {
        this.internalExtendedProperties = extendedProperties
    }
}