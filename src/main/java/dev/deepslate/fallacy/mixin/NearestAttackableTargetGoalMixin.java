package dev.deepslate.fallacy.mixin;

import dev.deepslate.fallacy.behavior.Behavior;
import dev.deepslate.fallacy.behavior.Behaviors;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class NearestAttackableTargetGoalMixin<T extends LivingEntity> extends TargetGoal {
    @Final
    @Shadow
    protected Class<T> targetType;

    @Nullable
    @Shadow
    protected LivingEntity target;
    @Shadow
    protected TargetingConditions targetConditions;

    public NearestAttackableTargetGoalMixin(Mob mob, boolean mustSee) {
        super(mob, mustSee);
    }

    @Shadow
    protected abstract AABB getTargetSearchArea(double targetDistance);

    @Inject(method = "findTarget", at = @At("HEAD"), cancellable = true)
    void injectFindTarget(CallbackInfo ci) {
        if (targetType != Player.class && targetType != ServerPlayer.class) {
            target = mob.level().getNearestEntity(
                    mob.level().getEntitiesOfClass(targetType, getTargetSearchArea(getFollowDistance()), entity -> true),
                    targetConditions, mob, mob.getX(), mob.getEyeY(), mob.getZ()
            );
        } else {
            target = fallacy$getNearbyPlayer();
        }
        ci.cancel();
    }

    @Unique
    private Boolean fallacy$checkMobType() {
        return mob instanceof Zombie || mob instanceof Skeleton || mob instanceof Creeper || mob instanceof Spider;
    }

    @Unique
    private Boolean fallacy$shouldAttackUndead(Player player) {
        if (!fallacy$checkMobType()) return true;
        return !Behavior.Companion.has((ServerPlayer) player, Behaviors.INSTANCE.getUNDEAD());
    }

    @Unique
    @Nullable
    private Player fallacy$getNearbyPlayer() {
        var players = mob.level().players();
        var entity = (Player) null;

        double d0 = -1.0;

        for (var p : players) {
            if (targetConditions.test(mob, p) && fallacy$shouldAttackUndead(p)) {
                double dis = p.distanceToSqr(mob.getX(), mob.getEyeY(), mob.getZ());
                if (d0 == -1.0 || dis < d0) {
                    d0 = dis;
                    entity = p;
                }
            }
        }

        return entity;
    }
}
