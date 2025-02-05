package dev.deepslate.fallacy.weather.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.weather.Weather
import net.minecraft.resources.ResourceLocation

class Rain : Weather() {
    companion object {
        val ID = Fallacy.id("rain")
    }

    override val isWet: Boolean = true

    override val namespaceId: ResourceLocation = ID
}