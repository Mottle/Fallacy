package dev.deepslate.fallacy.inject;

import dev.deepslate.fallacy.thermodynamics.ThermodynamicsEngine;
import kotlin.NotImplementedError;

public interface FallacyThermodynamicsExtension {
    default ThermodynamicsEngine fallacy$getThermodynamicsEngine() {
        throw new NotImplementedError("FallacyHeatEngineExtension#fallacy$getThermodynamicsEngine not implemented. This will never happen.");
    }

    default void fallacy$setThermodynamicsEngine(ThermodynamicsEngine engine) {
        throw new NotImplementedError("FallacyHeatEngineExtension#fallacy$setThermodynamicsEngine not implemented. This will never happen.");
    }
}
