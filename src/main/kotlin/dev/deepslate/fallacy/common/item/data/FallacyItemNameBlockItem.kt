package dev.deepslate.fallacy.common.item.data

import dev.deepslate.fallacy.common.item.FallacyBlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

class FallacyItemNameBlockItem(block: Block, properties: Item.Properties, extendedProperties: ExtendedProperties) :
    FallacyBlockItem(block, properties, extendedProperties) {

    override fun getDescriptionId(): String = orCreateDescriptionId
}