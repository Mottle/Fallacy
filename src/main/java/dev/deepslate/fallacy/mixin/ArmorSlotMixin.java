package dev.deepslate.fallacy.mixin;

import dev.deepslate.fallacy.common.data.FallacyDataComponents;
import dev.deepslate.fallacy.rule.item.ForceBindItemRule;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ArmorSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArmorSlot.class)
public abstract class ArmorSlotMixin {

    @Shadow
    @Final
    public EquipmentSlot slot;

    @Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
    void injectMayPickup(Player player, CallbackInfoReturnable<Boolean> cir) {
        var armorSlot = (ArmorSlot) (Object) this;
        var item = armorSlot.getItem();
        if (ForceBindItemRule.INSTANCE.check(slot, item)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "setByPlayer", at = @At("HEAD"), cancellable = true)
    void injectSetByPlayer(ItemStack newStack, ItemStack oldStack, CallbackInfo ci) {
        if (oldStack.has(FallacyDataComponents.INSTANCE.getFORCE_BINDING())) ci.cancel();
    }

//    @Unique
//    void fallacy$forceSet(ItemStack newStack, ItemStack oldStack) {
//        owner.onEquipItem(this.slot, oldStack, newStack);
//        super.setByPlayer(newStack, oldStack);
//    }
}
