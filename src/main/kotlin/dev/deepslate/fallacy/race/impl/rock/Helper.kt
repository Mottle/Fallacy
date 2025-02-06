package dev.deepslate.fallacy.race.impl.rock

import dev.deepslate.fallacy.common.item.FallacyItemTags
import dev.deepslate.fallacy.common.item.FallacyItems
import dev.deepslate.fallacy.common.item.component.FallacyDataComponents
import dev.deepslate.fallacy.race.impl.rock.Rock.Companion.CLADDING_LIMIT
import dev.deepslate.fallacy.race.impl.rock.Rock.Companion.claddingEffectMap
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.Unit
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.item.enchantment.ItemEnchantments

object Helper {
    fun forceBind(stack: ItemStack, lookup: HolderLookup.RegistryLookup<Enchantment>): ItemStack {
        val bindingCurse = lookup.get(Enchantments.BINDING_CURSE).get()
        val vanishCurse = lookup.get(Enchantments.VANISHING_CURSE).get()

        stack.enchant(bindingCurse, 1)
        stack.enchant(vanishCurse, 1)
        stack.set(FallacyDataComponents.FORCE_BINDING, Unit.INSTANCE)
        return stack
    }

    fun checkBroken(itemStack: ItemStack): Boolean = with(itemStack) {
        `is`(FallacyItems.Race.ROCK_SKIN_BROKEN_HELMET) || `is`(FallacyItems.Race.ROCK_SKIN_BROKEN_CHESTPLATE)
                || `is`(FallacyItems.Race.ROCK_SKIN_BROKEN_LEGGINGS)
                || `is`(FallacyItems.Race.ROCK_SKIN_BROKEN_BOOTS)
    }

    fun getMaxCladdingCount(id: ResourceLocation) = claddingEffectMap[id]?.maxCount ?: Int.MAX_VALUE

    fun checkCladding(armor: ItemStack, carried: ItemStack): Boolean {
        if (!armor.has(FallacyDataComponents.CLADDINGS)) return false
        if (carried.tags.noneMatch { it == FallacyItemTags.CLADDINGABLE }) return false

        val claddings = armor.get(FallacyDataComponents.CLADDINGS)!!
        val id = BuiltInRegistries.ITEM.getKey(carried.item)
        val maxCount = getMaxCladdingCount(id)

        if (claddings.claddingCount >= CLADDING_LIMIT) return false
        if (claddings.count(id) >= maxCount) return false

        return true
    }

    private fun applyModifiers(
        stack: ItemStack,
        map: Map<Holder<Attribute>, AttributeModifier>,
        slot: EquipmentSlot
    ) {
        val group = EquipmentSlotGroup.bySlot(slot)
        val default = (stack.item as? ArmorItem)?.defaultAttributeModifiers?.modifiers ?: return
        val new = default + map.map { (att, mod) -> ItemAttributeModifiers.Entry(att, mod, group) }
        val newModifier = ItemAttributeModifiers(new, true)

        stack.set(DataComponents.ATTRIBUTE_MODIFIERS, newModifier)
    }

    private fun applyEnchantments(stack: ItemStack, map: Map<Holder<Enchantment>, Int>) {
        //检查默认附魔
        if (map.size == 2) return
        val enc = ItemEnchantments.Mutable(ItemEnchantments.EMPTY)
        map.forEach { (holder, level) ->
            enc.set(holder, level)
        }
        stack.set(DataComponents.ENCHANTMENTS, enc.toImmutable())
    }

    fun applyCladding(player: ServerPlayer, stack: ItemStack) {
        if (checkBroken(stack)) return
        val slot = player.getEquipmentSlotForItem(stack)
        val lookup = player.registryAccess().lookup(Registries.ENCHANTMENT).get()

        val claddings = stack.get(FallacyDataComponents.CLADDINGS) ?: return
        val modifiers = claddings.getModifiers(claddingEffectMap, slot)
        val enchantments = claddings.getEnchantments(claddingEffectMap, lookup)

        applyModifiers(stack, modifiers, slot)
        applyEnchantments(stack, enchantments)
    }
}