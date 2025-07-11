package dev.deepslate.fallacy.client.tooltip

import com.mojang.datafixers.util.Either
import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.capability.FallacyCapabilities
import dev.deepslate.fallacy.common.data.player.FoodHistory
import dev.deepslate.fallacy.common.item.FallacyItemTags
import dev.deepslate.fallacy.common.item.component.FallacyDataComponents
import dev.deepslate.fallacy.util.extension.extendedProperties
import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RenderTooltipEvent

@EventBusSubscriber(modid = Fallacy.Companion.MOD_ID, value = [Dist.CLIENT])
object TooltipHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun onDeprecatedTooltip(event: RenderTooltipEvent.GatherComponents) {
        val stack = event.itemStack
        val item = stack.item
        val extendedProperties = item.extendedProperties
        if ((extendedProperties != null && extendedProperties.deprecated) || stack.has(FallacyDataComponents.DEPRECATED)) {
            event.tooltipElements.add(
                Either.left(
                    Component.translatable("item.tooltips.deprecated")
                        .withStyle(ChatFormatting.BOLD, ChatFormatting.RED, ChatFormatting.ITALIC)
                )
            )
        }

        if (stack.has(FallacyDataComponents.OUTDATED)) {
            event.tooltipElements.add(
                Either.left(
                    Component.translatable("item.tooltips.outdated")
                        .withStyle(ChatFormatting.BOLD, ChatFormatting.RED, ChatFormatting.ITALIC)
                )
            )
        }
    }

    @SubscribeEvent
    fun onFoodTooltip(event: RenderTooltipEvent.GatherComponents) {
        val stack = event.itemStack
        if (!stack.has(DataComponents.FOOD)) return

        val player = Minecraft.getInstance().player ?: return
        val diet = player.getCapability(FallacyCapabilities.DIET)!!
        val eatTimes = diet.history.countFood(stack)
        val nutrition = stack.get(FallacyDataComponents.NUTRITION)
        val fullLevel = stack.get(FallacyDataComponents.FULL_LEVEL) ?: 2

        event.tooltipElements.add(
            Either.left(
                Component.translatable("item.tooltips.full_level$fullLevel")
                    .withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN)
            )
        )

        if (nutrition != null) {
            addNutrition(event, "item.fallacy.diet_data.carbohydrate", nutrition.carbohydrate)
            addNutrition(event, "item.fallacy.diet_data.protein", nutrition.protein)
            addNutrition(event, "item.fallacy.diet_data.fat", nutrition.fat)
            addNutrition(event, "item.fallacy.diet_data.fiber", nutrition.fiber)
            addNutrition(event, "item.fallacy.diet_data.electrolyte", nutrition.electrolyte)
        }

        if (eatTimes != 0) {
            event.tooltipElements.add(
                Either.left(
                    Component.translatable("item.tooltips.food_eat_times", FoodHistory.Companion.MAX_SIZE, eatTimes)
                        .withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_PURPLE)
                )
            )
        } else {
            event.tooltipElements.add(
                Either.left(
                    Component.translatable("item.tooltips.food_never_eat")
                        .withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_PURPLE)
                )
            )
        }
    }

    private fun addNutrition(event: RenderTooltipEvent.GatherComponents, translateKey: String, value: Float) {
        if (value <= 0) return
        val formated = "%.1f".format(value)
        event.tooltipElements.add(
            Either.left(
                Component.translatable(translateKey).withStyle(ChatFormatting.ITALIC, ChatFormatting.BLUE)
                    .append(Component.literal(": $formated%").withStyle(ChatFormatting.ITALIC, ChatFormatting.BLUE))
            )
        )
    }

    @SubscribeEvent
    fun onRockTypeTooltip(event: RenderTooltipEvent.GatherComponents) {
        val stack = event.itemStack
        val existedTags = stack.tags.filter(::matchRockTag).toList()

        if (existedTags.isEmpty()) return

        val tag = existedTags.first()
        val key = when (tag) {
            FallacyItemTags.IGNEOUS_ROCK -> "item.fallacy.tooltips.igneous_rock"
            FallacyItemTags.METAMORPHIC_ROCK -> "item.fallacy.tooltips.metamorphic_rock"
            else -> "item.fallacy.tooltips.sedimentary_rock"
        }

        event.tooltipElements.add(
            Either.left(
                Component.translatable(key)
                    .withStyle(ChatFormatting.BOLD, ChatFormatting.WHITE)
            )
        )
    }

    private fun matchRockTag(tag: TagKey<Item>) = with(FallacyItemTags) {
        tag == SEDIMENTARY_ROCK || tag == IGNEOUS_ROCK || tag == METAMORPHIC_ROCK
    }

    @SubscribeEvent
    fun onRankTooltip(event: RenderTooltipEvent.GatherComponents) {
        val stack = event.itemStack
        val extendedProperties = stack.item.extendedProperties ?: return
        val rank = extendedProperties.rank
    }
}