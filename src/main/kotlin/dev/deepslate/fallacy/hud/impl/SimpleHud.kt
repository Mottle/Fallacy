package dev.deepslate.fallacy.hud.impl

import dev.deepslate.fallacy.util.RGB
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import kotlin.math.ceil

abstract class SimpleHud(name: String) : HudLayerImpl(name) {

    abstract val icon: ResourceLocation

    abstract val barColor: RGB

    abstract fun getValue(player: Player): Float

    abstract fun getMax(player: Player): Float

    abstract fun getHudValue(player: Player): Float

    override fun shouldRender(player: Player): Boolean = true

    override var isRightHandSide: Boolean = true

    override fun renderBar(
        graphic: GuiGraphics,
        player: Player,
        screenWidth: Int,
        screenHeight: Int,
        vOffset: Int
    ) {
        val barWidth = getBarWidth(player)
        val xStart = screenWidth / 2 + hOffset
        val yStart = screenHeight - vOffset

        RGB.reset()
        renderFullBackground(graphic, xStart, yStart)

        val barFrom = xStart + if (isRightHandSide) WIDTH - barWidth else 0
        getPrimaryBarColor(0, player).pushGL()

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
        val xStart = screenWidth / 2 + iconOffset
        val yStart = screenHeight - vOffset
        val color = getPrimaryBarColor(0, player)

        drawValue(graphic, xStart, yStart, getHudValue(player), color.value)
    }

    override fun getPrimaryBarColor(
        index: Int,
        player: Player
    ): RGB = barColor

    override fun renderIcon(
        graphic: GuiGraphics,
        player: Player,
        screenWidth: Int,
        screenHeight: Int,
        vOffset: Int
    ) {
        val xStart = screenWidth / 2 + iconOffset
        val yStart = screenHeight - vOffset
        graphic.blitSprite(icon, xStart, yStart, 9, 9)
    }

    override fun getBarWidth(player: Player): Int {
        return ceil(WIDTH * getValue(player) / getMax(player)).toInt()
    }
}