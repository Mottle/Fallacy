package dev.deepslate.fallacy.weather.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.weather.Weather
import net.minecraft.resources.ResourceLocation

object Clear : Weather() {
    override val namespaceId: ResourceLocation = Fallacy.withID("clear")
}