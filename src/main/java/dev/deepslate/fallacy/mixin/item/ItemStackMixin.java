package dev.deepslate.fallacy.mixin.item;

import dev.deepslate.fallacy.common.item.data.ExtendedProperties;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Unique
    private ItemStack fallacy$self() {
        return (ItemStack) (Object) this;
    }

//    @Inject(method = "<init>(Lnet/minecraft/world/level/ItemLike;ILnet/minecraft/core/component/PatchedDataComponentMap;)V", at = @At("TAIL"))
//    void mixinInit(ItemLike item, int count, PatchedDataComponentMap components, CallbackInfo ci) {
//        ExtendedProperties.Companion.onItemStack(fallacy$self());
//    }
}
