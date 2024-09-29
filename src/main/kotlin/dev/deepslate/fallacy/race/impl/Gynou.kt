package dev.deepslate.fallacy.race.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.player.NutritionState
import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import dev.deepslate.fallacy.common.item.component.FallacyDataComponents
import dev.deepslate.fallacy.race.Race
import dev.deepslate.fallacy.race.Respawnable
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.Unit
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.Unbreakable
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent

class Gynou : Race, Respawnable {

    companion object {
        val ID = Fallacy.id("gynou")

        fun getWings(lookup: HolderLookup.RegistryLookup<Enchantment>): ItemStack {
            val bindingCurse = lookup.get(Enchantments.BINDING_CURSE).get()
            val vanishCurse = lookup.get(Enchantments.VANISHING_CURSE).get()
            val item = ItemStack(Items.ELYTRA)

            item.enchant(bindingCurse, 1)
            item.enchant(vanishCurse, 1)
            item.set(DataComponents.UNBREAKABLE, Unbreakable(true))
            item.set(FallacyDataComponents.GYNOU_WINGS, Unit.INSTANCE)
            item.set(FallacyDataComponents.FORCE_BINDING, Unit.INSTANCE)
            item.set(DataComponents.ITEM_NAME, Component.translatable("item.fallacy.gynou_wings"))
            return item
        }
    }

    override val namespacedId: ResourceLocation = ID

    override val attribute: PlayerAttribute = PlayerAttribute(
        health = 20.0,
        attackDamage = 2.5,
        attackSpeed = 5.0,
        armor = -2.0,
        strength = 0.75,
        jumpStrength = 0.42 * 2.5,
        safeFallDistance = 3.0 * 2.5,
        fallDamageMultiplier = 0.5
    )

    override val diet: NutritionState = NutritionState()

    override fun tick(
        level: ServerLevel,
        player: ServerPlayer,
        position: BlockPos
    ) {
    }

    override fun set(player: ServerPlayer) {
        setWings(player)
    }

    override fun onRespawn(player: ServerPlayer, original: ServerPlayer) {
        setWings(player)
    }

    override fun remove(player: ServerPlayer) {
        player.setItemSlot(EquipmentSlot.CHEST, Items.AIR.defaultInstance)
    }

    private fun setWings(player: ServerPlayer) {
        val chest = player.getItemBySlot(EquipmentSlot.CHEST)

        if (!chest.has(FallacyDataComponents.GYNOU_WINGS)) {
            player.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY)
            player.drop(chest, true)
        }

        val lookup = player.registryAccess().lookup(Registries.ENCHANTMENT).get()
        val wings = getWings(lookup)
        player.setItemSlot(EquipmentSlot.CHEST, wings)
    }

    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object Handler {
        @SubscribeEvent
        fun onDamage(event: LivingIncomingDamageEvent) {
            val entity = event.entity
            val damage = event.container

            if (entity.level().isClientSide) return
            if (entity !is ServerPlayer) return
            if (!damage.source.`is`(DamageTypes.FLY_INTO_WALL)) return

            damage.newDamage /= 4f
        }
    }
}