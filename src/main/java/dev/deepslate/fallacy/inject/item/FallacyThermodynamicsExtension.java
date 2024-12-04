package dev.deepslate.fallacy.inject.item;

import dev.deepslate.fallacy.thermodynamics.ThermodynamicsEngine;
import kotlin.NotImplementedError;

public interface FallacyThermodynamicsExtension {
    default ThermodynamicsEngine fallacy$getThermodynamicsEngine() {
        throw new NotImplementedError("FallacyHeatEngineExtension#fallacy$getThermodynamicsEngine not implemented. This will never happen.");
    }
}
