package dev.deepslate.fallacy.rule.item

import dev.deepslate.fallacy.common.item.data.ExtendedProperties
import dev.deepslate.fallacy.util.extendedProperties
import dev.deepslate.fallacy.util.internalExtendedProperties
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items

object VanillaItemDeprecationRule {

    private fun setDeprecated(item: Item) {
        val properties =
            item.extendedProperties?.copy(isDeprecated = true) ?: ExtendedProperties.Builder().deprecated().build()
        item.internalExtendedProperties = properties
    }

    fun rule() {
        setDeprecated(Items.WHEAT_SEEDS)
        setDeprecated(Items.BEETROOT_SEEDS)
        setDeprecated(Items.POTATO)
        setDeprecated(Items.CARROT)
        setDeprecated(Items.MELON_SEEDS)
        setDeprecated(Items.PUMPKIN_SEEDS)
    }
}