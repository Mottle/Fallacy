package dev.deepslate.fallacy.hud.impl

import dev.deepslate.fallacy.util.RGB
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player

class ArmorHud : HudLayerImpl("armor") {

    companion object {
        val ICON = ResourceLocation.withDefaultNamespace("hud/armor_full")
    }

    override fun shouldRender(player: Player): Boolean = player.armorValue >= 1

    override fun renderBar(
        graphic: GuiGraphics,
        player: Player,
        screenWidth: Int,
        screenHeight: Int,
        vOffset: Int
    ) {
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
        val armor = player.armorValue
        val color = getPrimaryBarColor(0, player)

        drawValue(graphic, xStart, yStart, armor, color.value)
    }

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

    override fun getBarWidth(player: Player): Int = 0

    override fun getPrimaryBarColor(
        index: Int,
        player: Player
    ): RGB = RGB.from(255, 255, 255)
}