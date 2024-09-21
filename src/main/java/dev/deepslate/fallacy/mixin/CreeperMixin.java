package dev.deepslate.fallacy.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//Creeper 爆炸不再破坏方块
@Mixin(Creeper.class)
public abstract class CreeperMixin extends Monster {

    @Shadow
    private int explosionRadius;

    protected CreeperMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }


    @Shadow
    protected abstract void spawnLingeringCloud();

    @Shadow
    public abstract boolean isPowered();

    @Inject(method = "explodeCreeper", at = @At("HEAD"), cancellable = true)
    void injectExplodeCreeper(CallbackInfo ci) {
        if (!this.level().isClientSide) {
            float f = this.isPowered() ? 2.0F : 1.0F;
            this.dead = true;
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), (float) this.explosionRadius * f, Level.ExplosionInteraction.NONE);
            this.spawnLingeringCloud();
            this.triggerOnDeathMobEffects(Entity.RemovalReason.KILLED);
            this.discard();
        }

        ci.cancel();
    }
}
