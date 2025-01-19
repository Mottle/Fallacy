package dev.deepslate.fallacy.inject;

import dev.deepslate.fallacy.common.biome.data.BiomeSetting;
import kotlin.NotImplementedError;

public interface FallacyBiomeExtension {
    default BiomeSetting fallacy$getSetting() {
        throw new NotImplementedError("FallacyBiomeExtension#fallacy$getSetting is not implemented yet. This will never haven.");
    }

    default void fallacy$setSetting(BiomeSetting setting) {
        throw new NotImplementedError("FallacyBiomeExtension#fallacy$setSetting is not implemented yet. This will never haven.");
    }
}
