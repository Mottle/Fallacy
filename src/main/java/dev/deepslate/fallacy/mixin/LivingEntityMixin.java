package dev.deepslate.fallacy.mixin;

import dev.deepslate.fallacy.race.Race;
import dev.deepslate.fallacy.race.impl.Zombie;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(method = "isInvertedHealAndHarm", at = @At("HEAD"), cancellable = true)
    void injectIsInvertedHealAndHarm(CallbackInfoReturnable<Boolean> cir) {
        if (!((Object) this instanceof Player)) return;
        var player = (Player) (Object) this;
        var race = Race.Companion.get(player);
        cir.setReturnValue(race instanceof Zombie);
    }
}
