package dev.deepslate.fallacy.common

import dev.deepslate.fallacy.Fallacy
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageType

object FallacyDamageTypes {
    val DEHYDRATION: ResourceKey<DamageType> = ResourceKey.create(Registries.DAMAGE_TYPE, Fallacy.withID("dehydration"))
}