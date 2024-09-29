package dev.deepslate.fallacy.client.hud.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.capability.FallacyCapabilities
import dev.deepslate.fallacy.util.RGB
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player

class HydrationHud : SimpleHud("hydration") {
    override val icon: ResourceLocation = Fallacy.id("thirst")

    override val barColor: RGB = RGB.fromHex("#0A3AE8")

    override fun getValue(player: Player): Float {
        val cap = player.getCapability(FallacyCapabilities.THIRST)!!
        return cap.value
    }

    override fun getMax(player: Player): Float {
        val cap = player.getCapability(FallacyCapabilities.THIRST)!!
        return cap.max
    }

    override fun getHudValue(player: Player): Float = getValue(player)
}

//class HydrationHud : BarLayerImpl("hydration") {
//
//    companion object {
//        private val ICON = ResourceLocation.withDefaultNamespace("hud/air")
//        private val COLOR = RGB.fromHex("#00E6E6")
//    }
//
//    override fun shouldRender(player: Player): Boolean = true
//
//    override var isRightHandSide: Boolean = true
//
//    override fun renderBar(
//        graphic: GuiGraphics,
//        player: Player,
//        screenWidth: Int,
//        screenHeight: Int,
//        vOffset: Int
//    ) {
//        RGB.reset()
//        val xStart = screenWidth / 2 + hOffset
//        val yStart = screenHeight - vOffset
//        val barWidth = getBarWidth(player)
//
//        renderFullBackground(graphic, xStart, yStart)
//
//        val barFrom = xStart + if (isRightHandSide) WIDTH - barWidth else 0
//        val color = getPrimaryBarColor(0, player)
//
//        color.color2GL()
//        renderPartialBar(graphic, barFrom + 2, yStart + 2, barWidth)
//    }
//
//    override fun renderText(
//        graphic: GuiGraphics,
//        player: Player,
//        screenWidth: Int,
//        screenHeight: Int,
//        vOffset: Int
//    ) {
//        val cap = player.getCapability(FallacyCapabilities.THIRST)!!
//        val thirst = cap.thirst
//
//        val xStart = screenWidth / 2 + iconOffset
//        val yStart = screenHeight - vOffset
//        val color = getPrimaryBarColor(0, player)
//
//        drawValue(graphic, xStart, yStart, thirst, color.value)
//    }
//
//    override fun getPrimaryBarColor(
//        index: Int,
//        player: Player
//    ): RGB = COLOR
//
//    override fun renderIcon(
//        graphic: GuiGraphics,
//        player: Player,
//        screenWidth: Int,
//        screenHeight: Int,
//        vOffset: Int
//    ) {
//        val xStart = screenWidth / 2 + iconOffset
//        val yStart = screenHeight - vOffset
//
//        graphic.blitSprite(ICON, xStart, yStart, 9, 9)
//    }
//
//    override fun getBarWidth(player: Player): Int {
//        val cap = player.getCapability(FallacyCapabilities.THIRST)!!
//        val thirst = cap.thirst
//        val maxThirst = cap.max
//        return ceil(WIDTH * thirst / maxThirst).toInt()
//    }
//}