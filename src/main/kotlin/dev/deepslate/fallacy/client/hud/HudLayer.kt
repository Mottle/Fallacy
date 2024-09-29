package dev.deepslate.fallacy.client.hud

import dev.deepslate.fallacy.util.RGB
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.world.entity.player.Player

interface HudLayer {
    var isErrored: Boolean

    var isRightHandSide: Boolean

    fun render(graphic: GuiGraphics, player: Player, screenWidth: Int, screenHeight: Int, vOffset: Int)

    fun getBarWidth(player: Player): Int

    fun getPrimaryBarColor(index: Int, player: Player): RGB

    fun getSecondaryBarColor(index: Int, player: Player): RGB

    val isFitted: Boolean

    val name: String
}