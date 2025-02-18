package dev.deepslate.fallacy.mixin;

import dev.deepslate.fallacy.inject.FallacyThermodynamicsExtension;
import dev.deepslate.fallacy.inject.FallacyWeatherExtension;
import dev.deepslate.fallacy.thermodynamics.ThermodynamicsEngine;
import dev.deepslate.fallacy.thermodynamics.impl.EnvironmentThermodynamicsEngine;
import dev.deepslate.fallacy.weather.WeatherEngine;
import dev.deepslate.fallacy.weather.WindEngine;
import dev.deepslate.fallacy.weather.current.CurrentSimulator;
import dev.deepslate.fallacy.weather.impl.ServerWeatherEngine;
import dev.deepslate.fallacy.weather.impl.ServerWindEngine;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin implements FallacyWeatherExtension, FallacyThermodynamicsExtension {
    @Unique
    protected ThermodynamicsEngine fallacy$thermodynamicsEngine = null;
    @Unique
    ServerWeatherEngine fallacy$engine = null;
    @Unique
    ServerWindEngine fallacy$windEngine = null;
    @Unique
    CurrentSimulator fallacy$currentSimulator = null;

    @Inject(method = "<init>", at = @At("TAIL"))
    void construct(MinecraftServer server, Executor dispatcher, LevelStorageSource.LevelStorageAccess levelStorageAccess, ServerLevelData serverLevelData, ResourceKey dimension, LevelStem levelStem, ChunkProgressListener progressListener, boolean isDebug, long biomeZoomSeed, List customSpawners, boolean tickTime, RandomSequences randomSequences, CallbackInfo ci) {
        fallacy$thermodynamicsEngine = new EnvironmentThermodynamicsEngine(fallacy$self());
    }

    @Override
    public ThermodynamicsEngine fallacy$getThermodynamicsEngine() {
        return fallacy$thermodynamicsEngine;
    }

    @Unique
    private ServerLevel fallacy$self() {
        return (ServerLevel) (Object) this;
    }

    @Inject(method = "advanceWeatherCycle", at = @At("HEAD"), cancellable = true)
    void injectAdvanceWeatherCycle(CallbackInfo ci) {
        //不使用Vanilla的天气
        ci.cancel();
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

    @Override
    public WeatherEngine fallacy$getWeatherEngine() {
        return fallacy$engine;
    }

    @Override
    public void fallacy$setThermodynamicsEngine(ThermodynamicsEngine engine) {
        fallacy$thermodynamicsEngine = engine;
    }
}
