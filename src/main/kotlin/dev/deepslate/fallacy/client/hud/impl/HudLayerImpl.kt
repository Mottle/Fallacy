package dev.deepslate.fallacy.client.hud.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.client.hud.HudLayer
import dev.deepslate.fallacy.util.RGB
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import kotlin.math.floor

abstract class HudLayerImpl(override val name: String) : HudLayer {
    companion object {
        const val WIDTH = 77

        const val HEIGHT = 5

        const val BAR_U = 2

        const val BAR_V = 11

        val ICON_BAR: ResourceLocation = ResourceLocation.fromNamespaceAndPath(Fallacy.MOD_ID, "textures/gui/bar.png")
    }

    override var isErrored: Boolean = false

    open fun shouldRender(player: Player): Boolean = true

    override var isRightHandSide: Boolean = false

    override fun render(graphic: GuiGraphics, player: Player, screenWidth: Int, screenHeight: Int, vOffset: Int) {
        if (!shouldRender(player)) return
        renderBar(graphic, player, screenWidth, screenHeight, vOffset)
        renderText(graphic, player, screenWidth, screenHeight, vOffset)
        renderIcon(graphic, player, screenWidth, screenHeight, vOffset)

        with(Minecraft.getInstance().gui) {
            if (isRightHandSide) {
                rightHeight += 10
            } else {
                leftHeight += 10
            }
        }
        RGB.reset()
    }

    abstract fun renderBar(graphic: GuiGraphics, player: Player, screenWidth: Int, screenHeight: Int, vOffset: Int)

//    protected open fun shouldFlash(player: Player): Boolean = false

    protected abstract fun renderText(
        graphic: GuiGraphics,
        player: Player,
        screenWidth: Int,
        screenHeight: Int,
        vOffset: Int
    )

    protected abstract fun renderIcon(
        graphic: GuiGraphics,
        player: Player,
        screenWidth: Int,
        screenHeight: Int,
        vOffset: Int
    )

    val hOffset: Int
        get() = if (isRightHandSide) 10 else -91

    val iconOffset: Int
        get() = if (isRightHandSide) 92 else -101

    fun renderBarBackground(graphic: GuiGraphics, player: Player, screenWidth: Int, screenHeight: Int, vOffset: Int) {
        val barWidth = getBarWidth(player)
        var xStart = screenWidth / 2 + hOffset
        val yStart = screenHeight - vOffset
        if (isFitted && isRightHandSide) xStart += WIDTH - barWidth.toInt()
        if (isFitted) drawScaledBarBackground(graphic, barWidth, xStart, yStart + 1)
        else renderFullBackground(graphic, xStart, yStart)
    }

    fun drawScaledBarBackground(graphic: GuiGraphics, barWidth: Int, x: Int, y: Int) {
//        if(rightHandSide) {
//            graphic.blit(ICON_BAR, x, y - 1, 0, 0, barWidth.toInt() + 2, 9)
//            graphic.blit(ICON_BAR, x + barWidth.toInt() + 2, y - 1, WIDTH + 2, 0, 2, 9)
//        } else {
//            graphic.blit(ICON_BAR, x, y - 1, 0, 0, barWidth.toInt() + 2, 9)
//            graphic.blit(ICON_BAR, x + barWidth.toInt() + 2, y - 1, WIDTH + 2, 0, 2, 9)
//        }
        graphic.blit(ICON_BAR, x, y - 1, 0, 0, barWidth.toInt() + 2, 9)
        graphic.blit(ICON_BAR, x + barWidth.toInt() + 2, y - 1, WIDTH + 2, 0, 2, 9)
    }

    fun <T : Number> drawValue(graphic: GuiGraphics, xStart: Int, yStart: Int, stat: T, color: Int) {
        val value = floor(stat.toFloat()).toInt().toString()
        val font = Minecraft.getInstance().font

        if (isRightHandSide) {
            graphic.drawString(font, value, xStart + 9 + 2, yStart - 1 + 2, color)
        } else {
            val len = font.width(value)
            graphic.drawString(font, value, xStart - 9 - len + 5 + 2, yStart - 1 + 2, color)
        }
    }

    fun renderFullBackground(graphic: GuiGraphics, xStart: Int, yStart: Int) {
        graphic.blit(ICON_BAR, xStart, yStart, 0, 0, WIDTH + 4, 9)
    }

    fun renderFullBar(graphic: GuiGraphics, xStart: Int, yStart: Int) {
        renderPartialBar(graphic, xStart, yStart, WIDTH)
    }

    fun renderPartialBar(graphic: GuiGraphics, xStart: Int, yStart: Int, barWidth: Int) {
        graphic.blit(ICON_BAR, xStart, yStart, BAR_U, BAR_V, barWidth, HEIGHT)
    }

    override fun getPrimaryBarColor(
        index: Int,
        player: Player
    ): RGB = RGB.BLACK

    override fun getSecondaryBarColor(
        index: Int,
        player: Player
    ): RGB = RGB.BLACK

    override val isFitted: Boolean = false

}