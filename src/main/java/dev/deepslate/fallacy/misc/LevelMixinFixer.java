package dev.deepslate.fallacy.misc;

import dev.deepslate.fallacy.inject.FallacyWeatherExtension;
import dev.deepslate.fallacy.weather.FallacyWeathers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.fml.loading.FMLEnvironment;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class LevelMixinFixer {
    public static boolean tryInjectGetThunderLevel(Level level, CallbackInfoReturnable<Float> cir) {
        if (FMLEnvironment.dist.isClient()) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player == null) {
                cir.setReturnValue(0f);
                return true;
            }
            var engine = ((FallacyWeatherExtension) level).fallacy$getWeatherEngine();
            if (engine == null) {
                cir.setReturnValue(0f);
                return true;
            }
            var weather = engine.getWeatherAt(player.blockPosition());
            if (weather.is(FallacyWeathers.INSTANCE.getTHUNDER())) cir.setReturnValue(1f);
            return true;
        }
        return false;
    }

    public static boolean tryInjectGetRainLevel(Level level, CallbackInfoReturnable<Float> cir) {
        //在客户端时，若玩家处于下雨区域则返回1f用于渲染下雨效果
        if (FMLEnvironment.dist.isClient()) {
            var player = Minecraft.getInstance().player;
            if (player == null) {
                cir.setReturnValue(0f);
                return true;
            }
            var engine = ((FallacyWeatherExtension) level).fallacy$getWeatherEngine();
            if (engine == null) {
                cir.setReturnValue(0f);
                return true;
            }
            if (engine.isWet(player.blockPosition())) cir.setReturnValue(1f);
            return true;
        }
        return false;
    }
}
