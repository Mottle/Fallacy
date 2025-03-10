package dev.deepslate.fallacy.mixin;

import dev.deepslate.fallacy.inject.FallacyWeatherExtension;
import dev.deepslate.fallacy.misc.LevelMixinFixer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public abstract class LevelMixin {

    @Shadow
    public float oThunderLevel;

    @Shadow
    public float thunderLevel;

    @Shadow
    public float oRainLevel;

    @Shadow
    public float rainLevel;

    //fix rain
    @Inject(method = "isRainingAt", at = @At("HEAD"), cancellable = true)
    void injectIsRainAt(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        var self = (Level) (Object) this;
        var engine = ((FallacyWeatherExtension) self).fallacy$getWeatherEngine();
        if (engine == null) {
            cir.setReturnValue(false);
            return;
        }
        if (!engine.isWet(pos)) {
            cir.setReturnValue(false);
            return;
        }
        if (!self.canSeeSky(pos)) {
            cir.setReturnValue(false);
            return;
        } else if (self.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, pos).getY() > pos.getY()) {
            cir.setReturnValue(false);
            return;
        } else {
            Biome biome = self.getBiome(pos).value();
            cir.setReturnValue(biome.getPrecipitationAt(pos) == Biome.Precipitation.RAIN);
            return;
        }
    }

    @Inject(method = "isRaining", at = @At("HEAD"), cancellable = true)
    void injectIsRain(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "isThundering", at = @At("HEAD"), cancellable = true)
    void injectIsThundering(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Inject(method = "getRainLevel", at = @At("HEAD"), cancellable = true)
    void injectGetRainLevel(float partialTicks, CallbackInfoReturnable<Float> cir) {
        var res = LevelMixinFixer.tryInjectGetRainLevel((Level) (Object) this, cir);
        if (res) return;

        cir.setReturnValue(0f);
    }

    @Inject(method = "setRainLevel", at = @At("HEAD"), cancellable = true)
    void injectSetRainLevel(float strength, CallbackInfo ci) {
        rainLevel = 0f;
        oRainLevel = 0f;
        ci.cancel();
    }

    @Inject(method = "getThunderLevel", at = @At("HEAD"), cancellable = true)
    void injectGetThunderLevel(float partialTicks, CallbackInfoReturnable<Float> cir) {
        var res = LevelMixinFixer.tryInjectGetThunderLevel((Level) (Object) this, cir);
        if (res) return;
        cir.setReturnValue(0f);
    }

    @Inject(method = "setThunderLevel", at = @At("HEAD"), cancellable = true)
    void injectSetThunderLevel(float strength, CallbackInfo ci) {
        thunderLevel = 0f;
        oThunderLevel = 0f;
        ci.cancel();
    }
}
