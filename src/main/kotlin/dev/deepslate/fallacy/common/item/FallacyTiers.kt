package dev.deepslate.fallacy.common.item

import dev.deepslate.fallacy.common.block.FallacyBlockTags
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.SimpleTier

object FallacyTiers {
    val FLINT =
        SimpleTier(FallacyBlockTags.INCORRECT_FOR_DIGGER_LEVEL_0, 61, 2.5f, 0.5f, 0) { Ingredient.of() }

    val BONE =
        SimpleTier(FallacyBlockTags.INCORRECT_FOR_DIGGER_LEVEL_0, 117, 3.5f, 1.0f, 0) { Ingredient.of() }

    val FOSSIL =
        SimpleTier(FallacyBlockTags.INCORRECT_FOR_DIGGER_LEVEL_1, 253, 4.5f, 2.1f, 0) { Ingredient.of() }
}