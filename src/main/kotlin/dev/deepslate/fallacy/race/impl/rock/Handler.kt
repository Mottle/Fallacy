package dev.deepslate.fallacy.race.impl.rock

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.item.FallacyItems
import dev.deepslate.fallacy.common.item.component.CladdingData
import dev.deepslate.fallacy.common.item.component.FallacyDataComponents
import dev.deepslate.fallacy.common.network.packet.CladdingPacket
import dev.deepslate.fallacy.race.Race
import dev.deepslate.fallacy.race.impl.rock.Helper.applyCladding
import dev.deepslate.fallacy.race.impl.rock.Helper.forceBind
import dev.deepslate.fallacy.race.impl.rock.Rock.Companion.SKIN_REGENERATION_TICKS
import dev.deepslate.fallacy.race.impl.rock.Rock.Companion.claddingEffectMap
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.Unbreakable
import net.minecraft.world.item.enchantment.Enchantment
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent
import net.neoforged.neoforge.network.handling.IPayloadContext
import kotlin.math.pow

@EventBusSubscriber(modid = Fallacy.Companion.MOD_ID)
object Handler {
    internal fun handleCladdingPacket(data: CladdingPacket, context: IPayloadContext) {
        val armorIndex = data.index
        val player = context.player() as ServerPlayer
        val stack = player.inventory.getItem(armorIndex)
        val carried = player.containerMenu.carried

        if (!Helper.checkCladding(stack, carried)) return
        doCladding(stack, carried)
        applyCladding(player, stack)

        carried.count--
    }

    private fun doCladding(armor: ItemStack, carried: ItemStack) {
        val id = BuiltInRegistries.ITEM.getKey(carried.item)
        val duration = claddingEffectMap[id]!!.duration
        val claddings = armor.get(FallacyDataComponents.CLADDINGS)!!
        armor.set(FallacyDataComponents.CLADDINGS, claddings + CladdingData.Cladding(id, duration))
        carried.count--
    }

    private fun damageSkin(player: ServerPlayer, slot: EquipmentSlot, damage: Float) {
        val skin = player.getItemBySlot(slot)

        if (Helper.checkBroken(skin)) return

        skin.damageValue += damage.toInt()

        if (skin.damageValue >= skin.maxDamage) {
            player.onEquippedItemBroken(skin.item, slot)
            player.setItemSlot(slot, ItemStack.EMPTY)
        } else {
            damageCladding(player, skin)
        }
    }

    private fun damageCladding(player: ServerPlayer, skin: ItemStack) {
        val oldCladding = skin.get(FallacyDataComponents.CLADDINGS)!!
        val newCladdings = oldCladding.damageAll()
        skin.set(FallacyDataComponents.CLADDINGS, newCladdings)

        if (oldCladding.claddingCount != newCladdings.claddingCount) applyCladding(player, skin)
    }

    @SubscribeEvent
    fun onDamage(event: LivingDamageEvent.Pre) {
        val damage = event.source
        val player = event.entity as? ServerPlayer ?: return
        if (!player.isAlive) return
        if (Race.Companion.get(player) !is Rock) return

        if (damage.`is`(DamageTypeTags.BYPASSES_ARMOR)) return
        val lookup = player.registryAccess().lookup(Registries.DAMAGE_TYPE).get()

        val mobAttack = lookup.get(DamageTypes.MOB_ATTACK).get()
        val playerAttack = lookup.get(DamageTypes.PLAYER_ATTACK).get()
        val playerExplosion = lookup.get(DamageTypes.PLAYER_EXPLOSION).get()
        val sonicBoom = lookup.get(DamageTypes.SONIC_BOOM).get()
        val firework = lookup.get(DamageTypes.FIREWORKS).get()
        val fireball = lookup.get(DamageTypes.FIREBALL).get()
        val explosion = lookup.get(DamageTypes.EXPLOSION).get()
        val arrow = lookup.get(DamageTypes.ARROW).get()
        val set = setOf(mobAttack, playerAttack, playerExplosion, sonicBoom, firework, fireball, explosion, arrow)

        if (!set.contains(damage.typeHolder())) return

        val amount = event.originalDamage
        val armorExtraDamage = (amount / 8).pow(2)
        damageSkin(player, EquipmentSlot.HEAD, armorExtraDamage)
        damageSkin(player, EquipmentSlot.CHEST, armorExtraDamage)
        damageSkin(player, EquipmentSlot.LEGS, armorExtraDamage)
        damageSkin(player, EquipmentSlot.FEET, armorExtraDamage)
    }

    @SubscribeEvent
    fun onSkinBroken(event: LivingEquipmentChangeEvent) {
        val from = event.from
        val to = event.to
        val player = event.entity as? ServerPlayer ?: return

        if (from == to) return
        if (!to.isEmpty) return
        if (!from.has(FallacyDataComponents.CLADDINGS)) return
        if (Race.Companion.get(player) !is Rock) return

        val lookup = player.registryAccess().lookup(Registries.ENCHANTMENT).get()
        val slot = player.getEquipmentSlotForItem(from)
        val brokenSkin = getRockBrokenSkinItem(slot, lookup)

        brokenSkin.set(FallacyDataComponents.CLADDINGS, from.get(FallacyDataComponents.CLADDINGS)!!)
        player.setItemSlot(event.slot, brokenSkin)
    }

    private fun getRockBrokenSkinItem(
        slot: EquipmentSlot,
        lookup: HolderLookup.RegistryLookup<Enchantment>
    ): ItemStack {
        val item = when (slot) {
            EquipmentSlot.HEAD -> FallacyItems.RACE.ROCK_SKIN_BROKEN_HELMET
            EquipmentSlot.CHEST -> FallacyItems.RACE.ROCK_SKIN_BROKEN_CHESTPLATE
            EquipmentSlot.LEGS -> FallacyItems.RACE.ROCK_SKIN_BROKEN_LEGGINGS
            EquipmentSlot.FEET -> FallacyItems.RACE.ROCK_SKIN_BROKEN_BOOTS
            else -> return ItemStack.EMPTY
        }
        val stack = item.get().defaultInstance.let { forceBind(it, lookup) }

        stack.set(DataComponents.UNBREAKABLE, Unbreakable(false))
        stack.set(FallacyDataComponents.CLADDINGS, CladdingData.Companion.empty())
        stack.set(FallacyDataComponents.ROCK_SKIN_REGENERATION_REST_TICKS, SKIN_REGENERATION_TICKS)
        return stack
    }
}