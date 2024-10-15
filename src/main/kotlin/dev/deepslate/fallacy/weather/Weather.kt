package dev.deepslate.fallacy.weather

import net.minecraft.resources.ResourceLocation

interface Weather {
    fun tick()

    val namespaceId: ResourceLocation
}