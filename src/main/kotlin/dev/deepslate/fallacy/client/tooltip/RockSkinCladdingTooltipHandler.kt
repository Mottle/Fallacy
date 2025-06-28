package dev.deepslate.fallacy.client.tooltip

import com.mojang.datafixers.util.Either
import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.client.tooltip.component.ItemTooltipComponent
import dev.deepslate.fallacy.common.item.component.FallacyDataComponents
import dev.deepslate.fallacy.race.impl.rock.Rock
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FormattedText
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.Items
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RenderTooltipEvent
import kotlin.jvm.optionals.getOrNull

@EventBusSubscriber(modid = Fallacy.Companion.MOD_ID, value = [Dist.CLIENT])
object RockSkinCladdingTooltipHandler {

    @SubscribeEvent
    fun onRenderTooltips(event: RenderTooltipEvent.GatherComponents) {
        val stack = event.itemStack

        if (stack.item !is ArmorItem) return
        if (!stack.has(FallacyDataComponents.CLADDINGS)) return

        val tooltips = event.tooltipElements
        val claddings = stack.get(FallacyDataComponents.CLADDINGS) ?: return
        val additionalTooltips = claddings.claddings
            .map { (id, count) -> BuiltInRegistries.ITEM.getHolder(id).getOrNull()?.value() to count }
            .map { (item, duration) ->
//                (getNameForCladdingDisplay(item?.defaultInstance, duration) ?: Component.literal("???: duration"))
                ItemTooltipComponent(item ?: Items.STONE) to duration
            }
            .map { (tc, duration) -> Either.right<FormattedText, TooltipComponent>(tc.withPost(": $duration")) } +
                Either.left<FormattedText, TooltipComponent>(Component.literal("${claddings.claddingCount} / ${Rock.Companion.CLADDING_LIMIT}"))

        tooltips.addAll(1, additionalTooltips)
    }

//    private fun getNameForCladdingDisplay(stack: ItemStack?, duration: Int): Component? {
//        if (stack == null) return null
//
//        val defaultName = stack.displayName
//        val defaultStyle = defaultName.style
//        val rawName = defaultName.string
//        val name = if (rawName.startsWith("[") && rawName.endsWith("]")) rawName.substring(
//            1,
//            rawName.length - 1
//        ) else rawName
//        return Component.literal("$name: $duration").withStyle(defaultStyle)
//    }
}