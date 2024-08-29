package dev.deepslate.fallacy.common

import dev.deepslate.fallacy.Fallacy
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey

object FallacyDamageTypes {
    val DEHYDRATION = ResourceKey.create(Registries.DAMAGE_TYPE, Fallacy.id("dehydration"))
}