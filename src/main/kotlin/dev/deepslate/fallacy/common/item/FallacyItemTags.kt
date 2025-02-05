package dev.deepslate.fallacy.common.item

import dev.deepslate.fallacy.Fallacy
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item

object FallacyItemTags {
    private fun create(name: String) = ItemTags.create(Fallacy.id(name))

    val CLADDINGABLE: TagKey<Item> = create("claddingable")

    //火成岩
    val IGNEOUS_ROCK: TagKey<Item> = create("igneous_rock")

    //变质岩
    val METAMORPHIC_ROCK: TagKey<Item> = create("metamorphic_rock")

    //沉积岩
    val SEDIMENTARY_ROCK: TagKey<Item> = create("sedimentary_rock")

    val COAL: TagKey<Item> = create("coal")
}