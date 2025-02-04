package dev.deepslate.fallacy.inject;

import dev.deepslate.fallacy.weather.WeatherEngine;
import dev.deepslate.fallacy.weather.current.CurrentSimulator;
import dev.deepslate.fallacy.weather.WindEngine;
import kotlin.NotImplementedError;

public interface FallacyWeatherExtension {
    default void fallacy$setWeatherEngine(WeatherEngine engine) {
        throw new NotImplementedError("FallacyItemExtension#fallacy$setWeatherEngine not implemented. This will never happen.");
    }

    default WeatherEngine fallacy$getWeatherEngine() {
        throw new NotImplementedError("FallacyItemExtension#fallacy$getWeatherEngine not implemented. This will never happen.");
    }

    default void fallacy$setWindEngine(WindEngine engine) {
        throw new NotImplementedError("FallacyItemExtension#fallacy$setWindEngine not implemented. This will never happen.");
    }

    default WindEngine fallacy$getWindEngine() {
        throw new NotImplementedError("FallacyItemExtension#fallacy$getWindEngine not implemented. This will never happen.");
    }

    default void fallacy$setCurrentSimulator(CurrentSimulator simulator) {
        throw new NotImplementedError("FallacyItemExtension#fallacy$setCurrentSimulator not implemented. This will never happen.");
    }

    default CurrentSimulator fallacy$getCurrentSimulator() {
        throw new NotImplementedError("FallacyItemExtension#fallacy$getCurrentSimulator not implemented. This will never happen.");
    }
}
