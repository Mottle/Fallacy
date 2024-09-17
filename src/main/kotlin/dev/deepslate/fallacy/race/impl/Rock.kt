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
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.util.Unit
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.inventory.ArmorSlot
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RenderTooltipEvent
import net.neoforged.neoforge.client.event.ScreenEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext
import kotlin.jvm.optionals.getOrNull

class Rock : Race, Respawnable {

    companion object {
        val ID = Fallacy.id("rock")

        const val CLADDING_LIMIT = 8
    }

    override val namespacedId: ResourceLocation = ID

    override val attribute: PlayerAttribute = PlayerAttribute(
        armor = -20.0,
        health = 80.0,
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
    }

    private fun getRockSkinItem(slot: EquipmentSlot, lookup: HolderLookup.RegistryLookup<Enchantment>): ItemStack {
        val item = when (slot) {
            EquipmentSlot.HEAD -> FallacyItems.Race.ROCK_HELMET
            EquipmentSlot.CHEST -> FallacyItems.Race.ROCK_CHESTPLATE
            EquipmentSlot.LEGS -> FallacyItems.Race.ROCK_LEGGINGS
            EquipmentSlot.FEET -> FallacyItems.Race.ROCK_BOOTS
            else -> return ItemStack.EMPTY
        }
        val stack = item.get().defaultInstance
        val bindingCurse = lookup.get(Enchantments.BINDING_CURSE).get()
        val vanishCurse = lookup.get(Enchantments.VANISHING_CURSE).get()

        stack.enchant(bindingCurse, 1)
        stack.enchant(vanishCurse, 1)
        stack.set(FallacyDataComponents.FORCE_BINDING, Unit.INSTANCE)
//        stack.set(FallacyDataComponents.CLADDINGABLE, Unit.INSTANCE)
        stack.set(FallacyDataComponents.CLADDINGS, emptyList())

        val enc = stack.get(DataComponents.ENCHANTMENTS)!!.withTooltip(false)
        stack.set(DataComponents.ENCHANTMENTS, enc)

        return stack
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
        fun checkCladding(armor: ItemStack, carried: ItemStack): Boolean {
            if (!armor.has(FallacyDataComponents.CLADDINGS)) return false
            if (carried.tags.noneMatch { it == FallacyItemTags.CLADDINGABLE }) return false
            if (armor.get(FallacyDataComponents.CLADDINGS)!!.size >= CLADDING_LIMIT) return false
            return true
        }

        fun doCladding(armor: ItemStack, carried: ItemStack) {
            val id = BuiltInRegistries.ITEM.getKey(carried.item)
            val claddings = armor.get(FallacyDataComponents.CLADDINGS)!!
            armor.set(FallacyDataComponents.CLADDINGS, claddings + id)
            carried.count--
        }

        data class CladdingContext(val value: Double, val attribute: Holder<Attribute>)

        val claddingMap = mapOf(
            BuiltInRegistries.ITEM.getKey(Items.IRON_INGOT) to listOf(CladdingContext(0.2, Attributes.ARMOR)),
            BuiltInRegistries.ITEM.getKey(Items.GOLD_INGOT) to listOf(
                CladdingContext(0.000625 * 2, Attributes.MOVEMENT_SPEED),
                CladdingContext(-0.3125, Attributes.ARMOR)
            )
        )

        fun getCladdingModifiers(
            list: List<ResourceLocation>,
            slot: EquipmentSlot
        ): Map<Holder<Attribute>, AttributeModifier> {
            val materialCountMap = mutableMapOf<ResourceLocation, Int>()
            for (id in list) {
                if (!claddingMap.contains(id)) continue
                if (!materialCountMap.contains(id)) materialCountMap[id] = 0
                materialCountMap[id] = materialCountMap[id]!! + 1
            }
            val pairs = materialCountMap.map { (id, count) ->
                val contexts = claddingMap[id]!!
                val mods = contexts.map { context ->
                    context.attribute to count * context.value
                }.toMap()

                return@map mods
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

        fun applyModifier(stack: ItemStack, map: Map<Holder<Attribute>, AttributeModifier>, slot: EquipmentSlot) {
            val group = EquipmentSlotGroup.bySlot(slot)
            val default = (stack.item as? ArmorItem)?.defaultAttributeModifiers?.modifiers ?: return
            val new = default + map.map { (att, mod) -> ItemAttributeModifiers.Entry(att, mod, group) }
            val newModifier = ItemAttributeModifiers(new, true)

            stack.set(DataComponents.ATTRIBUTE_MODIFIERS, newModifier)
        }

        fun applyCladding(player: ServerPlayer, stack: ItemStack) {
            val slot = player.getEquipmentSlotForItem(stack)
            val claddings = stack.get(FallacyDataComponents.CLADDINGS) ?: return
            val modifiers = getCladdingModifiers(claddings, slot)
            applyModifier(stack, modifiers, slot)
        }

        fun applyAllArmor(player: ServerPlayer) {
            applyCladding(player, player.getItemBySlot(EquipmentSlot.HEAD))
            applyCladding(player, player.getItemBySlot(EquipmentSlot.CHEST))
            applyCladding(player, player.getItemBySlot(EquipmentSlot.LEGS))
            applyCladding(player, player.getItemBySlot(EquipmentSlot.FEET))
        }
    }

    //    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object Handler {
        fun handleCladdingPacket(data: CladdingPacket, context: IPayloadContext) {
            val armorIndex = data.index
            val player = context.player() as ServerPlayer
            val stack = player.inventory.getItem(armorIndex)
            val carried = player.containerMenu.carried

            if (!Helper.checkCladding(stack, carried)) return
            Helper.doCladding(stack, carried)
//            Helper.applyAllArmor(player)
            Helper.applyCladding(player, stack)

            carried.count--
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

        private fun getName(stack: ItemStack?): Component? {
            if (stack == null) return null

            val defaultName = stack.displayName
            val defaultStyle = defaultName.style
            val rawName = defaultName.string
            val name = if (rawName.startsWith("[") && rawName.endsWith("]")) rawName.substring(
                1,
                rawName.length - 1
            ) else rawName
            return Component.literal(name).withStyle(defaultStyle)
        }

        @SubscribeEvent
        fun onRenderTooltips(event: RenderTooltipEvent.GatherComponents) {
            val stack = event.itemStack

            if (stack.item !is ArmorItem) return
            if (!stack.has(FallacyDataComponents.CLADDINGS)) return

            val tooltips = event.tooltipElements
            val claddings = stack.get(FallacyDataComponents.CLADDINGS) ?: return
            val additionalTooltips = claddings
                .map { id -> BuiltInRegistries.ITEM.getHolder(id).getOrNull()?.value() }
                .map { getName(it?.defaultInstance) ?: Component.literal("???") }
                .map { Either.left<FormattedText, TooltipComponent>(it) } + Either.left<FormattedText, TooltipComponent>(
                Component.literal("${claddings.size} / $CLADDING_LIMIT")
            )

            tooltips.addAll(1, additionalTooltips)
        }
    }
}