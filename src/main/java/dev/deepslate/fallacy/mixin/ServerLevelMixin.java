package dev.deepslate.fallacy.mixin;

import dev.deepslate.fallacy.inject.FallacyWeatherExtension;
import dev.deepslate.fallacy.weather.ServerWeatherEngine;
import dev.deepslate.fallacy.weather.WeatherEngine;
import dev.deepslate.fallacy.weather.current.CurrentSimulator;
import dev.deepslate.fallacy.weather.wind.ServerWindEngine;
import dev.deepslate.fallacy.weather.wind.WindEngine;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin implements FallacyWeatherExtension {
    @Unique
    ServerWeatherEngine fallacy$engine = null;

    @Unique
    ServerWindEngine fallacy$windEngine = null;

    @Unique
    CurrentSimulator fallacy$currentSimulator = null;

    @Inject(method = "advanceWeatherCycle", at = @At("HEAD"), cancellable = true)
    void injectAdvanceWeatherCycle(CallbackInfo ci) {
        //不使用Vanilla的天气
        ci.cancel();
    }

    @Override
    public WeatherEngine fallacy$getWeatherEngine() {
        return fallacy$engine;
    }

    @Override
    public void fallacy$setWeatherEngine(WeatherEngine engine) {
        if (!(engine instanceof ServerWeatherEngine)) {
            throw new IllegalArgumentException("Cannot set weather engine to non-ServerLevelWeatherEngine for ServerLevel.");
        }
        fallacy$engine = (ServerWeatherEngine) engine;
    }

    @Override
    public void fallacy$setWindEngine(WindEngine engine) {
        if (!(engine instanceof ServerWindEngine)) {
            throw new IllegalArgumentException("Cannot set wind engine to non-ServerLevelWindEngine for ServerLevel.");
        }
        fallacy$windEngine = (ServerWindEngine) engine;
    }

    @Override
    public WindEngine fallacy$getWindEngine() {
        return fallacy$windEngine;
    }

    @Override
    public void fallacy$setCurrentSimulator(CurrentSimulator simulator) {
        fallacy$currentSimulator = simulator;
    }

    @Override
    public CurrentSimulator fallacy$getCurrentSimulator() {
        return fallacy$currentSimulator;
    }
}
