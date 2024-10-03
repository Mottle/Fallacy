package dev.deepslate.fallacy.mixin.item;

import dev.deepslate.fallacy.common.item.data.ExtendedProperties;
import dev.deepslate.fallacy.inject.item.FallacyItemExtension;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

@Mixin(Item.class)
public abstract class ItemExtensionMixin implements FallacyItemExtension {

    @Unique
    protected ExtendedProperties fallacy$extendedProperties = null;

    @Unique
    private Item fallacy$self() {
        return (Item) (Object) this;
    }

    @Nullable
    @Override
    public ExtendedProperties fallacy$getExtendedProperties() {
        return fallacy$extendedProperties;
    }

    @Override
    public void fallacy$setExtendedProperties(ExtendedProperties properties) {
        fallacy$extendedProperties = properties;
    }
}
