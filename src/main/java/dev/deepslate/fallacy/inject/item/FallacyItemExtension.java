package dev.deepslate.fallacy.inject.item;

import dev.deepslate.fallacy.common.item.data.ExtendedProperties;
import kotlin.NotImplementedError;

public interface FallacyItemExtension {
    default ExtendedProperties fallacy$getExtendedProperties() {
        throw new NotImplementedError("FallacyItemExtension#getExtendedProperties not implemented. This will never haven.");
    }

    default void fallacy$setExtendedProperties(ExtendedProperties properties) {
        throw new NotImplementedError("FallacyItemExtension#setExtendedProperties not implemented. This will never happen.");
    }
}
