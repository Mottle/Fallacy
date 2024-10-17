package dev.deepslate.fallacy.util.extension

import dev.deepslate.fallacy.inject.FallacyLevelWeatherExtension
import dev.deepslate.fallacy.weather.WeatherEngine
import net.minecraft.world.level.Level

internal var Level.internalWeatherEngine: WeatherEngine?
    get() = (this as FallacyLevelWeatherExtension).`fallacy$getWeatherEngine`()
    set(value) {
        (this as FallacyLevelWeatherExtension).`fallacy$setWeatherEngine`(value)
    }

val Level.weatherEngine: WeatherEngine?
    get() = this.internalWeatherEngine