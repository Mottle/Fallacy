package dev.deepslate.fallacy.common.item

import dev.deepslate.fallacy.Fallacy
import net.minecraft.tags.ItemTags

object FallacyItemTags {
    private fun create(name: String) = ItemTags.create(Fallacy.id(name))

    val CLADDINGABLE = create("claddingable")
}