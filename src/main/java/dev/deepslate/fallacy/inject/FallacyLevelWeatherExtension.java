package dev.deepslate.fallacy.inject;

import dev.deepslate.fallacy.weather.WeatherEngine;
import kotlin.NotImplementedError;

public interface FallacyLevelWeatherExtension {
    default void fallacy$setWeatherEngine(WeatherEngine engine) {
        throw new NotImplementedError("FallacyItemExtension#fallacy$setWeatherEngine not implemented. This will never happen.");
    }

    default WeatherEngine fallacy$getWeatherEngine() {
        throw new NotImplementedError("FallacyItemExtension#fallacy$getWeatherEngine not implemented. This will never happen.");
    }
}
