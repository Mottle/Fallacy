package dev.deepslate.fallacy.mixin.client;

import dev.deepslate.fallacy.common.data.FallacyDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements DataComponentHolder {

    //FORCE_BINDING存在时，不显示消失诅咒和绑定诅咒
    @Inject(method = "addToTooltip", at = @At("HEAD"), cancellable = true)
    <T extends TooltipProvider> void injectAddToTooltip(DataComponentType<T> component, Item.TooltipContext context, Consumer<Component> tooltipAdder, TooltipFlag tooltipFlag, CallbackInfo ci) {
        if (component != DataComponents.ENCHANTMENTS) return;
        if (!has(FallacyDataComponents.INSTANCE.getFORCE_BINDING())) return;

        var enc = this.get(DataComponents.ENCHANTMENTS);
        if (enc != null) {
            var showEnc = new ItemEnchantments.Mutable(enc);
            showEnc.removeIf(holder -> holder.getKey() == Enchantments.BINDING_CURSE || holder.getKey() == Enchantments.VANISHING_CURSE);
            showEnc.toImmutable().addToTooltip(context, tooltipAdder, tooltipFlag);
        }
        ci.cancel();
    }

    //显示Modifier Lore时，若为MOVEMENT_SPEED，则显示每20t速度，而非1t
    @Inject(method = "addModifierTooltip", at = @At("HEAD"), cancellable = true)
    void injectAddModifierTooltip(Consumer<Component> tooltipAdder, Player player, Holder<Attribute> attribute, AttributeModifier modifier, CallbackInfo ci) {
        if (attribute != Attributes.MOVEMENT_SPEED) return;

        var speedValuePerTick = modifier.amount();
        if (modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                || modifier.operation() == AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
            speedValuePerTick *= 100.0;
        }

        var speedValuePer20Tick = speedValuePerTick * 20.0;

        if (speedValuePer20Tick > 0.0) {
            tooltipAdder.accept(
                    Component.translatable(
                                    "attribute.modifier.plus." + modifier.operation().id(),
                                    ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(speedValuePer20Tick) + "/20t",
                                    Component.translatable(attribute.value().getDescriptionId())
                            )
                            .withStyle(attribute.value().getStyle(true))
            );
        } else if (speedValuePerTick < 0.0) {
            tooltipAdder.accept(
                    Component.translatable(
                                    "attribute.modifier.take." + modifier.operation().id(),
                                    ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(-speedValuePer20Tick) + "/20t",
                                    Component.translatable(attribute.value().getDescriptionId())
                            )
                            .withStyle(attribute.value().getStyle(false))
            );
        }
        ci.cancel();
    }
}
