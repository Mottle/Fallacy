package dev.deepslate.fallacy.race.impl.rock

import com.mojang.datafixers.util.Either
import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.item.component.FallacyDataComponents
import dev.deepslate.fallacy.common.network.packet.CladdingPacket
import dev.deepslate.fallacy.race.impl.rock.Rock.Companion.CLADDING_LIMIT
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen
import net.minecraft.client.gui.screens.inventory.InventoryScreen
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FormattedText
import net.minecraft.world.inventory.ArmorSlot
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ItemStack
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RenderTooltipEvent
import net.neoforged.neoforge.client.event.ScreenEvent
import net.neoforged.neoforge.network.PacketDistributor
import kotlin.jvm.optionals.getOrNull

@EventBusSubscriber(modid = Fallacy.Companion.MOD_ID, value = [Dist.CLIENT])
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
        val additionalTooltips = claddings.claddings
            .map { (id, count) -> BuiltInRegistries.ITEM.getHolder(id).getOrNull()?.value() to count }
            .map { (item, count) ->
                (getNameForCladdingDisplay(item?.defaultInstance, count) ?: Component.literal("???: $count"))
            }
            .map { Either.left<FormattedText, TooltipComponent>(it) } + Either.left<FormattedText, TooltipComponent>(
            Component.literal("${claddings.claddingCount} / $CLADDING_LIMIT")
        )

        tooltips.addAll(1, additionalTooltips)
    }

    private fun getNameForCladdingDisplay(stack: ItemStack?, duration: Int): Component? {
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
}