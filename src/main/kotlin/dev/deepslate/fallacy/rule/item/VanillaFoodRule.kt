package dev.deepslate.fallacy.rule.item

import dev.deepslate.fallacy.common.item.data.ExtendedFoodProperties
import dev.deepslate.fallacy.common.item.data.ExtendedProperties
import dev.deepslate.fallacy.util.extendedProperties
import net.minecraft.world.item.Items

object VanillaFoodRule {
    fun rule() {
        Items.ROTTEN_FLESH.extendedProperties = ExtendedProperties.Builder().withFoodProperties(
            ExtendedFoodProperties.Builder().withEatenDurationTicks(60).build()
        ).build()
    }
} 