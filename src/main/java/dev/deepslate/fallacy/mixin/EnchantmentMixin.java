package dev.deepslate.fallacy.mixin;

import dev.deepslate.fallacy.rule.item.EnchantmentRule;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {

    @Inject(method = "modifyDamage", at = @At("TAIL"))
    void injectModifyDamage(ServerLevel level, int enchantmentLevel, ItemStack tool, Entity entity, DamageSource damageSource, MutableFloat damage, CallbackInfo ci) {
        var lookup = level.registryAccess().lookup(Registries.ENCHANTMENT).orElse(null);
        if (lookup == null) return;
        var enchantments = tool.getAllEnchantments(lookup);
        for (var holder : enchantments.keySet()) {
            EnchantmentRule.INSTANCE.modifyDamage(damage, holder, enchantmentLevel, entity);
        }
    }
}
