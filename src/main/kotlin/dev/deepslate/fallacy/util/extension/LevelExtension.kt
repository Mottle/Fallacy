package dev.deepslate.fallacy.util.extension

import dev.deepslate.fallacy.inject.FallacyWeatherExtension
import dev.deepslate.fallacy.weather.WeatherEngine
import dev.deepslate.fallacy.weather.WindEngine
import dev.deepslate.fallacy.weather.current.CurrentSimulator
import net.minecraft.world.level.Level

internal var Level.internalWeatherEngine: WeatherEngine?
    get() = (this as FallacyWeatherExtension).`fallacy$getWeatherEngine`()
    set(value) {
        (this as FallacyWeatherExtension).`fallacy$setWeatherEngine`(value)
    }

val Level.weatherEngine: WeatherEngine?
    get() = this.internalWeatherEngine

internal var Level.internalWindEngine: WindEngine?
    get() = (this as FallacyWeatherExtension).`fallacy$getWindEngine`()
    set(value) {
        (this as FallacyWeatherExtension).`fallacy$setWindEngine`(value)
    }

val Level.windEngine: WindEngine?
    get() = this.internalWindEngine

internal var Level.internalCurrentSimulator: CurrentSimulator?
    get() = (this as FallacyWeatherExtension).`fallacy$getCurrentSimulator`()
    set(value) {
        (this as FallacyWeatherExtension).`fallacy$setCurrentSimulator`(value)
    }

val Level.currentSimulator: CurrentSimulator?
    get() = this.internalCurrentSimulator