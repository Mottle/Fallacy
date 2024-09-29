package dev.deepslate.fallacy.mixin.player;

import com.mojang.authlib.GameProfile;
import dev.deepslate.fallacy.common.data.player.ExtendedFoodData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin {

    @Shadow
    protected FoodData foodData;

    @Unique
    Player fallacy$self() {
        return (Player) (Object) this;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    void mixinInit(Level level, BlockPos pos, float yRot, GameProfile gameProfile, CallbackInfo ci) {
        foodData = new ExtendedFoodData(fallacy$self());
    }
}
