package dev.deepslate.fallacy.race.impl.rock

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.player.NutritionState
import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import dev.deepslate.fallacy.common.item.FallacyItems
import dev.deepslate.fallacy.common.item.component.CladdingData
import dev.deepslate.fallacy.common.item.component.FallacyDataComponents
import dev.deepslate.fallacy.race.Race
import dev.deepslate.fallacy.race.Respawnable
import dev.deepslate.fallacy.race.impl.rock.Helper.applyCladding
import dev.deepslate.fallacy.race.impl.rock.Helper.forceBind
import dev.deepslate.fallacy.race.resources.AttributeResource
import dev.deepslate.fallacy.race.resources.NutritionResource
import dev.deepslate.fallacy.util.TickHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import kotlin.math.pow

class Rock : Race(), Respawnable {
    companion object {
        val ID = Fallacy.Companion.withID("rock")

        const val CLADDING_LIMIT = 8

        val SKIN_REGENERATION_TICKS = TickHelper.second(10)
    }

    override val namespacedId: ResourceLocation = ID

    override val resources: Map<String, Resource> = mapOf(
        AttributeResource.KEY to AttributeResource(
            PlayerAttribute(
                armor = -20.0,
                health = 60.0,
                attackDamage = 8.0,
                attackKnockBack = 1.0,
                magicResistance = 50.0,
                moveSpeed = 9.0 / 100.0,
                strength = 8.0,
                gravity = 0.08 * 1.5,
                jumpStrength = 0.42 * 1.195, // Magic number
                fallDamageMultiplier = 1.5,
                scale = 2.0.pow(1.0 / 3.0),
                entityInteractionRange = 3.0 * 2.0.pow(1.0 / 3.0),
                blockInteractionRange = 4.5 * 2.0.pow(1.0 / 3.0),
                knockBackResistance = 0.2,
                hunger = 60.0
            )
        ),
        NutritionResource.KEY to NutritionResource(NutritionState.noNeed())
    )

    override fun tick(
        level: ServerLevel,
        player: ServerPlayer,
        position: BlockPos
    ) {
        if (player.combatTracker.inCombat) return
        if (!TickHelper.checkServerTickRate(TickHelper.second(4))) return
        repair(player, EquipmentSlot.HEAD)
        repair(player, EquipmentSlot.CHEST)
        repair(player, EquipmentSlot.LEGS)
        repair(player, EquipmentSlot.FEET)
    }

    private fun repair(player: ServerPlayer, slot: EquipmentSlot) {
        val item = player.getItemBySlot(slot)
        if (item.isEmpty) return
        if (!item.has(FallacyDataComponents.CLADDINGS)) return
        if (Helper.checkBroken(item)) {
            if (!item.has(FallacyDataComponents.ROCK_SKIN_REGENERATION_REST_TICKS)) return
            val restTicks = item.get(FallacyDataComponents.ROCK_SKIN_REGENERATION_REST_TICKS)!! - TickHelper.second(4)

            if (restTicks <= 0) regenerate(player, item, slot)
            else item.set(FallacyDataComponents.ROCK_SKIN_REGENERATION_REST_TICKS, restTicks)

            return
        }
        item.damageValue -= 1
    }

    private fun regenerate(player: ServerPlayer, old: ItemStack, slot: EquipmentSlot) {
        val lookup = player.registryAccess().lookup(Registries.ENCHANTMENT).get()
        player.setItemSlot(slot, ItemStack.EMPTY)
        setArmor(player, slot, lookup)
        val new = player.getItemBySlot(slot)
        copyCladding(old, new)
    }

    private fun setArmor(player: ServerPlayer, slot: EquipmentSlot, lookup: HolderLookup.RegistryLookup<Enchantment>) {
        val default = player.getItemBySlot(slot)
        val stack = getRockSkinItem(slot, lookup)

        if (!default.`is`(stack.item)) {
            player.setItemSlot(slot, ItemStack.EMPTY)
            player.drop(default, true)
        }

        player.setItemSlot(slot, stack)
    }

    private fun getRockSkinItem(slot: EquipmentSlot, lookup: HolderLookup.RegistryLookup<Enchantment>): ItemStack {
        val item = when (slot) {
            EquipmentSlot.HEAD -> FallacyItems.RACE.ROCK_SKIN_HELMET
            EquipmentSlot.CHEST -> FallacyItems.RACE.ROCK_SKIN_CHESTPLATE
            EquipmentSlot.LEGS -> FallacyItems.RACE.ROCK_SKIN_LEGGINGS
            EquipmentSlot.FEET -> FallacyItems.RACE.ROCK_SKIN_BOOTS
            else -> return ItemStack.EMPTY
        }
        val stack = item.get().defaultInstance.let { forceBind(it, lookup) }

        stack.set(FallacyDataComponents.CLADDINGS, CladdingData.Companion.empty())

        return stack
    }

    private fun setAllArmor(player: ServerPlayer) {
        val lookup = player.registryAccess().lookup(Registries.ENCHANTMENT).get()
        setArmor(player, EquipmentSlot.HEAD, lookup)
        setArmor(player, EquipmentSlot.CHEST, lookup)
        setArmor(player, EquipmentSlot.LEGS, lookup)
        setArmor(player, EquipmentSlot.FEET, lookup)
    }

    private fun removeArmor(player: ServerPlayer, slot: EquipmentSlot) {
        if (player.getItemBySlot(slot).has(FallacyDataComponents.FORCE_BINDING)) player.setItemSlot(
            slot,
            ItemStack.EMPTY
        )
    }

    private fun removeAllArmor(player: ServerPlayer) {
        removeArmor(player, EquipmentSlot.HEAD)
        removeArmor(player, EquipmentSlot.CHEST)
        removeArmor(player, EquipmentSlot.LEGS)
        removeArmor(player, EquipmentSlot.FEET)
    }

    // newTotal = total * (1.0 - 0.75)
    private val attackSpeedModifier = AttributeModifier(
        Fallacy.Companion.withID("rock_race_attack_speed_modifier"),
        -0.75,
        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
    )

    override fun apply(player: ServerPlayer) {
        setAllArmor(player)
        player.getAttribute(Attributes.ATTACK_SPEED)!!.addPermanentModifier(attackSpeedModifier)
    }

    override fun deapply(player: ServerPlayer) {
        player.getAttribute(Attributes.ATTACK_SPEED)!!.removeModifier(attackSpeedModifier)
        removeAllArmor(player)
    }

    override fun onRespawn(player: ServerPlayer) {
        player.getAttribute(Attributes.ATTACK_SPEED)!!.addPermanentModifier(attackSpeedModifier)
    }

    override fun onClone(
        player: ServerPlayer,
        origin: ServerPlayer
    ) {
        setAllArmor(player)
        copyEquipment(origin, player, EquipmentSlot.HEAD)
        copyEquipment(origin, player, EquipmentSlot.CHEST)
        copyEquipment(origin, player, EquipmentSlot.LEGS)
        copyEquipment(origin, player, EquipmentSlot.FEET)
        applyAllArmor(player)
    }

    private fun applyAllArmor(player: ServerPlayer) {
        applyCladding(player, player.getItemBySlot(EquipmentSlot.HEAD))
        applyCladding(player, player.getItemBySlot(EquipmentSlot.CHEST))
        applyCladding(player, player.getItemBySlot(EquipmentSlot.LEGS))
        applyCladding(player, player.getItemBySlot(EquipmentSlot.FEET))
    }

    private fun copyEquipment(from: ServerPlayer, to: ServerPlayer, slot: EquipmentSlot) {
        val stackFrom = from.getItemBySlot(slot)
        val stackTo = to.getItemBySlot(slot)
        if (!stackFrom.has(FallacyDataComponents.CLADDINGS)) return
        copyCladding(stackFrom, stackTo)
    }

    private fun copyCladding(from: ItemStack, to: ItemStack) {
        if (!from.has(FallacyDataComponents.CLADDINGS)) return
        to.set(FallacyDataComponents.CLADDINGS, from.get(FallacyDataComponents.CLADDINGS))
    }
}