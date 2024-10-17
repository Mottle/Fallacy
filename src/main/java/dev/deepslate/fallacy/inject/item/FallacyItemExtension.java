package dev.deepslate.fallacy.inject.item;

import dev.deepslate.fallacy.common.item.data.ExtendedProperties;
import kotlin.NotImplementedError;

public interface FallacyItemExtension {
    default ExtendedProperties fallacy$getExtendedProperties() {
        throw new NotImplementedError("FallacyItemExtension#fallacy$getExtendedProperties not implemented. This will never happen.");
    }

    default void fallacy$setExtendedProperties(ExtendedProperties properties) {
        throw new NotImplementedError("FallacyItemExtension#fallacy$setExtendedProperties not implemented. This will never happen.");
    }
}
