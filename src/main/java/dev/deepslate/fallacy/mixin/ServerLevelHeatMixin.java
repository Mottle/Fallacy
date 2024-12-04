package dev.deepslate.fallacy.mixin;

import dev.deepslate.fallacy.inject.item.FallacyThermodynamicsExtension;
import dev.deepslate.fallacy.thermodynamics.EnvironmentThermodynamicsEngine;
import dev.deepslate.fallacy.thermodynamics.ThermodynamicsEngine;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerLevel.class)
public abstract class ServerLevelHeatMixin implements FallacyThermodynamicsExtension {

    @Unique
    protected ThermodynamicsEngine fallacy$thermodynamicsEngine = new EnvironmentThermodynamicsEngine(fallacy$self());

    @Unique
    private ServerLevel fallacy$self() {
        return (ServerLevel) (Object) this;
    }

    @Override
    public ThermodynamicsEngine fallacy$getThermodynamicsEngine() {
        return fallacy$thermodynamicsEngine;
    }
}
