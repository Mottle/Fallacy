package dev.deepslate.fallacy.common.registrate

import com.tterrag.registrate.builders.BlockBuilder
import com.tterrag.registrate.builders.ItemBuilder
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block

fun <T : Item, P> ItemBuilder<T, P>.formattedLang(): ItemBuilder<T, P> =
    lang(Item::getDescriptionId, name.replace('_', ' '))

fun <T : Block, P> BlockBuilder<T, P>.formattedLang(): BlockBuilder<T, P> =
    lang(Block::getDescriptionId, name.replace('_', ' '))
