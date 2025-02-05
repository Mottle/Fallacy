package dev.deepslate.fallacy.mixin.client;

import dev.deepslate.fallacy.inject.FallacyWeatherExtension;
import dev.deepslate.fallacy.weather.WeatherEngine;
import dev.deepslate.fallacy.weather.WindEngine;
import dev.deepslate.fallacy.weather.current.CurrentSimulator;
import dev.deepslate.fallacy.weather.impl.ClientWeatherEngine;
import dev.deepslate.fallacy.weather.impl.ClientWindEngine;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin implements FallacyWeatherExtension {
    @Unique
    private ClientWeatherEngine fallacy$engine = null;

    @Unique
    private ClientWindEngine fallacy$windEngine = null;

    @Override
    public void fallacy$setWeatherEngine(WeatherEngine engine) {
        if (!(engine instanceof ClientWeatherEngine)) {
            throw new IllegalArgumentException("Cannot set weather engine to non-ClientLevelWeatherEngine for ClientLevel.");
        }
        fallacy$engine = (ClientWeatherEngine) engine;
    }

    @Override
    public WeatherEngine fallacy$getWeatherEngine() {
        return fallacy$engine;
    }

    @Override
    public void fallacy$setWindEngine(WindEngine engine) {
        if (!(engine instanceof ClientWindEngine)) {
            throw new IllegalArgumentException("Cannot set wind engine to non-ClientLevelWindEngine for ClientLevel.");
        }

        fallacy$windEngine = (ClientWindEngine) engine;
    }

    @Override
    public WindEngine fallacy$getWindEngine() {
        return fallacy$windEngine;
    }

    @Override
    public void fallacy$setCurrentSimulator(CurrentSimulator simulator) {
    }

    @Override
    public CurrentSimulator fallacy$getCurrentSimulator() {
        return null;
    }
}
