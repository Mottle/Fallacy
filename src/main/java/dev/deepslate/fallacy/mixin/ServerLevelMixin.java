package dev.deepslate.fallacy.mixin;

import dev.deepslate.fallacy.inject.FallacyLevelWeatherExtension;
import dev.deepslate.fallacy.weather.ServerWeatherEngine;
import dev.deepslate.fallacy.weather.WeatherEngine;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin implements FallacyLevelWeatherExtension {
    @Unique
    ServerWeatherEngine fallacy$engine = null;

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
}
