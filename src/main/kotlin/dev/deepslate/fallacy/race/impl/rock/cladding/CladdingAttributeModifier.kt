package dev.deepslate.fallacy.race.impl.rock.cladding

import net.minecraft.core.Holder
import net.minecraft.world.entity.ai.attributes.Attribute

data class CladdingAttributeModifier(val attribute: Holder<Attribute>, val value: Double) : CladdingEffect