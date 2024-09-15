package dev.deepslate.fallacy.mixin;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(RangedAttribute.class)
public abstract class RangedAttributeMixin {
    @Mutable
    @Shadow @Final private double maxValue;

    @Mutable
    @Shadow @Final private double minValue;

    @Inject(method = "<init>", at = @At("TAIL"))
    void mixinInit(String descriptionId, double defaultValue, double min, double max, CallbackInfo ci) {
        if(Objects.equals(descriptionId, "attribute.name.generic.armor")) {
            this.minValue = -1024.0;
            this.maxValue = 1024.0;
        }

        if(Objects.equals(descriptionId, "attribute.name.generic.armor_toughness")) {
            this.minValue = -256.0;
            this.maxValue = 256.0;
        }

        if(Objects.equals(descriptionId, "attribute.name.generic.max_health")) {
            this.maxValue = 32784.0;
        }

        if(Objects.equals(descriptionId, "attribute.name.generic.attack_damage")) {
            this.maxValue = Float.MAX_VALUE;
        }
    }
}
