package dev.deepslate.fallacy.hud.impl

import dev.deepslate.fallacy.util.RGB
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import kotlin.math.ceil

class AirHud : HudLayerImpl("air") {

    companion object {
        private val ICON = ResourceLocation.withDefaultNamespace("hud/air")
        private val COLOR = RGB.fromHex("#00E6E6")
    }

    override fun shouldRender(player: Player): Boolean = player.airSupply < player.maxAirSupply

    override var isRightHandSide: Boolean = true

    override fun renderBar(
        graphic: GuiGraphics,
        player: Player,
        screenWidth: Int,
        screenHeight: Int,
        vOffset: Int
    ) {
        val xStart = screenWidth / 2 + hOffset
        val yStart = screenHeight - vOffset
        val barWidth = getBarWidth(player)

        RGB.reset()
        renderFullBackground(graphic, xStart, yStart)

        val barFrom = xStart + if (isRightHandSide) WIDTH - barWidth else 0
        val color = getPrimaryBarColor(0, player)

        color.color2GL()
        renderPartialBar(graphic, barFrom + 2, yStart + 2, barWidth)
        RGB.reset()
    }

    override fun renderText(
        graphic: GuiGraphics,
        player: Player,
        screenWidth: Int,
        screenHeight: Int,
        vOffset: Int
    ) {
        val air = player.airSupply
        val xStart = screenWidth / 2 + iconOffset
        val yStart = screenHeight - vOffset
        val color = getPrimaryBarColor(0, player)

        drawValue(graphic, xStart, yStart, air / 20, color.value)
    }

    override fun getPrimaryBarColor(
        index: Int,
        player: Player
    ): RGB = COLOR

    override fun renderIcon(
        graphic: GuiGraphics,
        player: Player,
        screenWidth: Int,
        screenHeight: Int,
        vOffset: Int
    ) {
        val xStart = screenWidth / 2 + iconOffset
        val yStart = screenHeight - vOffset

        graphic.blitSprite(ICON, xStart, yStart, 9, 9)
    }

    override fun getBarWidth(player: Player): Int {
        val air = player.airSupply
        val maxAir = player.maxAirSupply
        return ceil(WIDTH * air.toFloat() / maxAir.toFloat()).toInt()
    }
}