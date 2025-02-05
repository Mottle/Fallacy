package dev.deepslate.fallacy.mixin.item;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

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
