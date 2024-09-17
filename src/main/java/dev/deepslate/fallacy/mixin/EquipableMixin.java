package dev.deepslate.fallacy.mixin;

import dev.deepslate.fallacy.rule.item.ForceBindItemRule;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Equipable.class)
public interface EquipableMixin {
    @Inject(method = "swapWithEquipmentSlot", at = @At("HEAD"), cancellable = true)
    default void injectSwapWithEquipmentSlot(Item item, Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        var stack = player.getItemInHand(hand);
        var slot = player.getEquipmentSlotForItem(stack);
        var armor = player.getItemBySlot(slot);
        if (ForceBindItemRule.INSTANCE.check(slot, armor)) cir.setReturnValue(InteractionResultHolder.fail(stack));
    }
}
