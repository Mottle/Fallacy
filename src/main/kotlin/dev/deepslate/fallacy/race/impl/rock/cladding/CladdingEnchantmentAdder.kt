package dev.deepslate.fallacy.race.impl.rock.cladding

import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment


data class CladdingEnchantmentAdder(val enchantment: ResourceKey<Enchantment>, val level: Int) : CladdingEffect