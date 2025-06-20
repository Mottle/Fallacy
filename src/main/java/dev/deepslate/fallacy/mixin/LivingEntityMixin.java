package dev.deepslate.fallacy.mixin;

import dev.deepslate.fallacy.race.Race;
import dev.deepslate.fallacy.race.Races;
import dev.deepslate.fallacy.race.impl.Zombie;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Unique
    private LivingEntity fallacy$self() {
        return (LivingEntity) (Object) this;
    }

    @Inject(method = "isInvertedHealAndHarm", at = @At("HEAD"), cancellable = true)
    void injectIsInvertedHealAndHarm(CallbackInfoReturnable<Boolean> cir) {
        if (!(fallacy$self() instanceof Player player)) return;

        var race = Race.Companion.get(player);
        cir.setReturnValue(race instanceof Zombie);
    }

    //蜘蛛种族可以爬行
    @Inject(method = "onClimbable", at = @At("RETURN"), cancellable = true)
    void injectOnClimbable(CallbackInfoReturnable<Boolean> cir) {
        if (!(fallacy$self() instanceof Player player)) return;
        if (player.isSpectator()) return;

        var race = Race.Companion.get(player);
        var isSpiderRace = race.getNamespacedId().equals(Races.INSTANCE.getSPIDER().getId());
        var horizontalCollision = player.horizontalCollision;
        var isSneaky = player.isShiftKeyDown(); //提高体验，只有蹲下时才能攀爬
        var shouldClimb = isSpiderRace && horizontalCollision && isSneaky;

        cir.setReturnValue(cir.getReturnValue() || shouldClimb);
    }
}
