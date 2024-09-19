package dev.deepslate.fallacy.race.impl

import com.mojang.datafixers.util.Either
import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.FallacyDataComponents
import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import dev.deepslate.fallacy.common.item.FallacyItemTags
import dev.deepslate.fallacy.common.item.FallacyItems
import dev.deepslate.fallacy.common.network.packet.CladdingPacket
import dev.deepslate.fallacy.race.Race
import dev.deepslate.fallacy.race.Respawnable
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FormattedText
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.tags.DamageTypeTags
import net.minecraft.util.Unit
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.inventory.ArmorSlot
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.item.component.Unbreakable
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.item.enchantment.ItemEnchantments
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RenderTooltipEvent
import net.neoforged.neoforge.client.event.ScreenEvent
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.map
import kotlin.jvm.optionals.getOrNull
import kotlin.math.pow

class Rock : Race, Respawnable {

    sealed interface CladdingEffect

    data class CladdingAttributeModifier(val attribute: Holder<Attribute>, val value: Double) : CladdingEffect

    data class CladdingEnchantmentAdder(val enchantment: ResourceKey<Enchantment>, val level: Int) : CladdingEffect

    data class CladdingInfo(val duration: Int, val effects: List<CladdingEffect>, val maxCount: Int = Int.MAX_VALUE)

    companion object {
        val ID = Fallacy.id("rock")

        const val CLADDING_LIMIT = 8

        private val claddingEffectMap: Map<ResourceLocation, CladdingInfo> = mapOf(
            getKey(Items.IRON_INGOT) to CladdingInfo(125, listOf(CladdingAttributeModifier(Attributes.ARMOR, 0.2))),
            getKey(Items.GOLD_INGOT) to CladdingInfo(
                40, listOf(
                    CladdingAttributeModifier(Attributes.MOVEMENT_SPEED, 0.000625 * 2),
                    CladdingAttributeModifier(Attributes.ARMOR, -0.3125)
                )
            ),
            getKey(Items.DIAMOND) to CladdingInfo(
                600, listOf(
                    CladdingAttributeModifier(Attributes.ARMOR, 0.8),
                )
            ),
            getKey(Items.OBSIDIAN) to CladdingInfo(
                1200, listOf(
                    CladdingEnchantmentAdder(Enchantments.FIRE_PROTECTION, 1),
                    CladdingEnchantmentAdder(Enchantments.BLAST_PROTECTION, 1),
                    CladdingAttributeModifier(Attributes.ARMOR, 0.4),
                    CladdingAttributeModifier(Attributes.MAX_HEALTH, -1.0)
                )
            )
        )

        private fun getMaxCladdingCount(id: ResourceLocation) = claddingEffectMap[id]?.maxCount ?: Int.MAX_VALUE

        private fun getKey(item: Item): ResourceLocation = BuiltInRegistries.ITEM.getKey(item)
    }

    override val namespacedId: ResourceLocation = ID

    override val attribute: PlayerAttribute = PlayerAttribute(
        armor = -20.0,
        health = 60.0,
        attackDamage = 8.0,
        attackSpeed = 1.0,
        attackKnockBack = 1.0,
        magicResistance = 50.0,
        moveSpeed = 9.0 / 100.0,
        strength = 8.0,
        gravity = 0.08 * 1.5,
        jumpStrength = 0.42 * 1.21,
        fallDamageMultiplier = 1.5,
    )

    override fun tick(
        level: ServerLevel,
        player: ServerPlayer,
        position: BlockPos
    ) {
        if (player.combatTracker.inCombat) return

    }

    private fun setArmor(player: ServerPlayer, slot: EquipmentSlot, lookup: HolderLookup.RegistryLookup<Enchantment>) {
        val default = player.getItemBySlot(slot)
        val stack = Helper.getRockSkinItem(slot, lookup)

        if (!default.`is`(stack.item)) {
            player.setItemSlot(slot, ItemStack.EMPTY)
            player.drop(default, true)
        }

        player.setItemSlot(slot, stack)
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

    override fun set(player: ServerPlayer) {
        attribute.set(player)
        setAllArmor(player)
    }

    override fun remove(player: ServerPlayer) {
        removeAllArmor(player)
    }

    override fun onRespawn(player: ServerPlayer) {
        setAllArmor(player)
    }

    private object Helper {
        fun getRockSkinItem(slot: EquipmentSlot, lookup: HolderLookup.RegistryLookup<Enchantment>): ItemStack {
            val item = when (slot) {
                EquipmentSlot.HEAD -> FallacyItems.Race.ROCK_SKIN_HELMET
                EquipmentSlot.CHEST -> FallacyItems.Race.ROCK_SKIN_CHESTPLATE
                EquipmentSlot.LEGS -> FallacyItems.Race.ROCK_SKIN_LEGGINGS
                EquipmentSlot.FEET -> FallacyItems.Race.ROCK_SKIN_BOOTS
                else -> return ItemStack.EMPTY
            }
            val stack = item.get().defaultInstance
            val bindingCurse = lookup.get(Enchantments.BINDING_CURSE).get()
            val vanishCurse = lookup.get(Enchantments.VANISHING_CURSE).get()

            stack.enchant(bindingCurse, 1)
            stack.enchant(vanishCurse, 1)
            stack.set(FallacyDataComponents.FORCE_BINDING, Unit.INSTANCE)
            stack.set(FallacyDataComponents.CLADDINGS, emptyList())

            return stack
        }

        fun getRockBrokenSkinItem(old: ItemStack, lookup: HolderLookup.RegistryLookup<Enchantment>): ItemStack {
            val armorItem = old.item as? ArmorItem ?: return ItemStack.EMPTY
            val base = when (armorItem.equipmentSlot) {
                EquipmentSlot.HEAD -> FallacyItems.Race.ROCK_SKIN_BROKEN_HELMET
                EquipmentSlot.CHEST -> FallacyItems.Race.ROCK_SKIN_BROKEN_CHESTPLATE
                EquipmentSlot.LEGS -> FallacyItems.Race.ROCK_SKIN_BROKEN_LEGGINGS
                EquipmentSlot.FEET -> FallacyItems.Race.ROCK_SKIN_BROKEN_BOOTS
                else -> return ItemStack.EMPTY
            }
            val new = base.asItem().defaultInstance
            val oldCladding = old.get(FallacyDataComponents.CLADDINGS) ?: return ItemStack.EMPTY
            val bindingCurse = lookup.get(Enchantments.BINDING_CURSE).get()
            val vanishCurse = lookup.get(Enchantments.VANISHING_CURSE).get()

            new.enchant(bindingCurse, 1)
            new.enchant(vanishCurse, 1)
            new.set(FallacyDataComponents.FORCE_BINDING, Unit.INSTANCE)
            new.set(FallacyDataComponents.CLADDINGS, oldCladding)
            new.set(DataComponents.UNBREAKABLE, Unbreakable(false))

            return new
        }

        fun getNameForCladdingDisplay(stack: ItemStack?, duration: Int): Component? {
            if (stack == null) return null

            val defaultName = stack.displayName
            val defaultStyle = defaultName.style
            val rawName = defaultName.string
            val name = if (rawName.startsWith("[") && rawName.endsWith("]")) rawName.substring(
                1,
                rawName.length - 1
            ) else rawName
            return Component.literal("$name: $duration").withStyle(defaultStyle)
        }

        fun checkBroken(itemStack: ItemStack): Boolean = with(itemStack) {
            `is`(FallacyItems.Race.ROCK_SKIN_BROKEN_HELMET)
                    || `is`(FallacyItems.Race.ROCK_SKIN_BROKEN_CHESTPLATE)
                    || `is`(FallacyItems.Race.ROCK_SKIN_BROKEN_LEGGINGS)
                    || `is`(FallacyItems.Race.ROCK_SKIN_BROKEN_BOOTS)
        }

        fun checkCladding(armor: ItemStack, carried: ItemStack): Boolean {
            if (!armor.has(FallacyDataComponents.CLADDINGS)) return false
            if (carried.tags.noneMatch { it == FallacyItemTags.CLADDINGABLE }) return false

            val claddings = armor.get(FallacyDataComponents.CLADDINGS)!!
            val id = BuiltInRegistries.ITEM.getKey(carried.item)
            val maxCount = getMaxCladdingCount(id)

            if (claddings.size >= CLADDING_LIMIT) return false
            if (claddings.count { it == id } >= maxCount) return false

            return true
        }

        fun doCladding(armor: ItemStack, carried: ItemStack) {
            val id = BuiltInRegistries.ITEM.getKey(carried.item)
            val duration = claddingEffectMap[id]!!.duration
            val claddings = armor.get(FallacyDataComponents.CLADDINGS)!!
            armor.set(FallacyDataComponents.CLADDINGS, claddings + (id to duration))
            carried.count--
        }

        private fun getCladdingModifiers(
            list: List<ResourceLocation>,
            slot: EquipmentSlot
        ): Map<Holder<Attribute>, AttributeModifier> {
            val materialCountMap = countId(list)
            val pairs =
                countEffects<CladdingAttributeModifier, Holder<Attribute>, Double>(materialCountMap) { context, count ->
                    context.attribute to count * context.value
                }
            val finalMap = mutableMapOf<Holder<Attribute>, Double>()

            pairs.forEach {
                it.forEach { holder, value ->
                    if (!finalMap.contains(holder)) finalMap[holder] = 0.0
                    finalMap[holder] = finalMap[holder]!! + value
                }
            }

            return finalMap.map { (holder, value) ->
                holder to AttributeModifier(
                    Fallacy.id("rock_skin_cladding_${slot.serializedName}"),
                    value,
                    AttributeModifier.Operation.ADD_VALUE
                )
            }.toMap()
        }

        private fun getCladdingEnchantments(
            list: List<ResourceLocation>,
            lookup: HolderLookup.RegistryLookup<Enchantment>
        ): Map<Holder<Enchantment>, Int> {
            val materialCountMap = countId(list)
            val pairs =
                countEffects<CladdingEnchantmentAdder, ResourceKey<Enchantment>, Int>(materialCountMap) { context, count ->
                    context.enchantment to count * context.level
                }

            val enchantmentMap = pairs
                .map { it.toList() }
                .flatten()
                .groupBy { it.first }
                .mapValues { it.value.map { it.second } }
                .mapValues { it.value.sum() }

            //默认附魔
            val bindingCurse = lookup.get(Enchantments.BINDING_CURSE).get()
            val vanishCurse = lookup.get(Enchantments.VANISHING_CURSE).get()
            val defaultMap = mapOf(bindingCurse to 1, vanishCurse to 1)

            return enchantmentMap.mapKeys { lookup.get(it.key).get() } + defaultMap
        }

        private fun countId(list: List<ResourceLocation>): Map<ResourceLocation, Int> {
            val materialCountMap = mutableMapOf<ResourceLocation, Int>()
            for (id in list) {
                if (!claddingEffectMap.contains(id)) continue
                if (!materialCountMap.contains(id)) materialCountMap[id] = 0
                materialCountMap[id] = materialCountMap[id]!! + 1
            }
            return materialCountMap.toMap()
        }

        private inline fun <reified T : CladdingEffect, reified V, I> countEffects(
            materialCountMap: Map<ResourceLocation, Int>,
            contextHandler: (T, Int) -> Pair<V, I>
        ): List<Map<V, I>> = materialCountMap.map { (id, count) ->
            val contexts =
                claddingEffectMap[id]!!.effects.filter { it is T } as List<T>
            val mods = contexts.map { contextHandler(it, count) }.toMap()

            return@map mods
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

        fun applyEnchantments(stack: ItemStack, map: Map<Holder<Enchantment>, Int>) {
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
            val claddings = stack.get(FallacyDataComponents.CLADDINGS)?.map { it.first } ?: return
            val modifiers = getCladdingModifiers(claddings, slot)
            val enchantments = getCladdingEnchantments(claddings, lookup)

            applyModifiers(stack, modifiers, slot)
            applyEnchantments(stack, enchantments)
        }

//        fun applyAllArmor(player: ServerPlayer) {
//            applyCladding(player, player.getItemBySlot(EquipmentSlot.HEAD))
//            applyCladding(player, player.getItemBySlot(EquipmentSlot.CHEST))
//            applyCladding(player, player.getItemBySlot(EquipmentSlot.LEGS))
//            applyCladding(player, player.getItemBySlot(EquipmentSlot.FEET))
//        }
    }

    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object Handler {
        fun handleCladdingPacket(data: CladdingPacket, context: IPayloadContext) {
            val armorIndex = data.index
            val player = context.player() as ServerPlayer
            val stack = player.inventory.getItem(armorIndex)
            val carried = player.containerMenu.carried

            if (!Helper.checkCladding(stack, carried)) return
            Helper.doCladding(stack, carried)
            Helper.applyCladding(player, stack)

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
            val newCladdings = oldCladding.map { it.first to it.second - 1 }.filter { it.second > 0 }
            skin.set(FallacyDataComponents.CLADDINGS, newCladdings)

            if (oldCladding.size != newCladdings.size) Helper.applyCladding(player, skin)
        }

        @SubscribeEvent
        fun onDamage(event: LivingDamageEvent.Pre) {
            val damage = event.source
            val player = event.entity as? ServerPlayer ?: return
            if (Race.get(player) !is Rock) return

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
            if (Race.get(player) !is Rock) return

            val lookup = player.registryAccess().lookup(Registries.ENCHANTMENT).get()
            val brokenSkin = Helper.getRockBrokenSkinItem(from, lookup)
            player.setItemSlot(event.slot, brokenSkin)
        }
    }

    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object ClientHandler {

        @SubscribeEvent
        fun onCladding(event: ScreenEvent.MouseButtonPressed.Post) {
            val screen = event.screen

            if (screen !is InventoryScreen && screen !is CreativeModeInventoryScreen) return

            val slot = screen.slotUnderMouse ?: return
            if (screen is InventoryScreen && slot !is ArmorSlot) return

            val armor = slot.item
            val carried = screen.menu.carried

            if (!Helper.checkCladding(armor, carried)) return
            PacketDistributor.sendToServer(CladdingPacket(slot.slotIndex))
        }

        @SubscribeEvent
        fun onRenderTooltips(event: RenderTooltipEvent.GatherComponents) {
            val stack = event.itemStack

            if (stack.item !is ArmorItem) return
            if (!stack.has(FallacyDataComponents.CLADDINGS)) return

            val tooltips = event.tooltipElements
            val claddings = stack.get(FallacyDataComponents.CLADDINGS) ?: return
            val additionalTooltips = claddings
                .map { (id, count) -> BuiltInRegistries.ITEM.getHolder(id).getOrNull()?.value() to count }
                .map { (item, count) ->
                    (Helper.getNameForCladdingDisplay(item?.defaultInstance, count) ?: Component.literal("???: $count"))
                }
                .map { Either.left<FormattedText, TooltipComponent>(it) } + Either.left<FormattedText, TooltipComponent>(
                Component.literal("${claddings.size} / $CLADDING_LIMIT")
            )

            tooltips.addAll(1, additionalTooltips)
        }
    }
}