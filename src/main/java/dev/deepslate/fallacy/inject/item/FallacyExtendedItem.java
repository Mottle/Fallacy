package dev.deepslate.fallacy.inject.item;

import dev.deepslate.fallacy.common.item.data.ExtendedProperties;
import kotlin.NotImplementedError;

public interface FallacyExtendedItem {
    default ExtendedProperties fallacy$getExtendedProperties() {
        throw new NotImplementedError("FallacyExtendedItem#getExtendedProperties not implemented. This will never haven.");
    }

    default void fallacy$setExtendedProperties(ExtendedProperties properties) {
        throw new NotImplementedError("FallacyExtendedItem#setExtendedProperties not implemented. This will never happen.");
    }
}
