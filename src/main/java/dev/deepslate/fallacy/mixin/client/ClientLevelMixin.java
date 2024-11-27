package dev.deepslate.fallacy.mixin.client;

import dev.deepslate.fallacy.inject.FallacyWeatherExtension;
import dev.deepslate.fallacy.weather.ClientWeatherEngine;
import dev.deepslate.fallacy.weather.WeatherEngine;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin implements FallacyWeatherExtension {
    @Unique
    private ClientWeatherEngine fallacy$engine = null;

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
}
