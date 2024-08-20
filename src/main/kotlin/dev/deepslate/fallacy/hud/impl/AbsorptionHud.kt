package dev.deepslate.fallacy.hud.impl

import dev.deepslate.fallacy.hud.HealthEffect
import dev.deepslate.fallacy.util.RGB
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import kotlin.math.ceil
import kotlin.math.min

class AbsorptionHud : HudLayerImpl("absorption") {

    companion object {
        private val COLORS = listOf(
            "#D4AF37",
            "#C2C73B",
            "#8DC337",
            "#36BA77",
            "#4A5BC4",
            "#D89AE2",
            "#DF9DC7",
            "#DFA99D",
            "#D4DF9D",
            "#3E84C6",
            "#B8C1E8",
            "#DFDFDF"
        ).map(RGB::fromHex)

        private val POISON_COLORS = listOf(
            "#D4AF37",
            "#C2C73B",
            "#8DC337",
            "#36BA77",
            "#4A5BC4",
            "#D89AE2",
            "#DF9DC7",
            "#DFA99D",
            "#D4DF9D",
            "#3E84C6",
            "#B8C1E8",
            "#DFDFDF"
        ).map(RGB::fromHex)

        val WITHER_COLORS = listOf(
            "#D4AF37",
            "#C2C73B",
            "#8DC337",
            "#36BA77",
            "#4A5BC4",
            "#D89AE2",
            "#DF9DC7",
            "#DFA99D",
            "#D4DF9D",
            "#3E84C6",
            "#B8C1E8",
            "#DFDFDF"
        ).map(RGB::fromHex)

        private val ICON = ResourceLocation.withDefaultNamespace("hud/heart/absorbing_full")

        private val HARDCORE_ICON = ResourceLocation.withDefaultNamespace("hud/heart/absorbing_hardcore_full")

        private val CONTAINER = ResourceLocation.withDefaultNamespace("hud/heart/container")
    }

    override fun shouldRender(player: Player): Boolean = player.absorptionAmount > 0

    override val isFitted: Boolean = true

    override fun renderBar(
        graphic: GuiGraphics,
        player: Player,
        screenWidth: Int,
        screenHeight: Int,
        vOffset: Int
    ) {
        val absorption = player.absorptionAmount
        val barWidth = getBarWidth(player)
        val maxHealth = player.maxHealth

        val xStart = screenWidth / 2 + hOffset + if (isRightHandSide) WIDTH - barWidth else 0
        val yStart = screenHeight - vOffset

        val index = min(ceil(absorption / maxHealth).toInt(), COLORS.size) - 1
        val color = getPrimaryBarColor(index, player)

        RGB.reset()
        renderBarBackground(graphic, player, screenWidth, screenHeight, vOffset)

        if (index == 0) {
            color.pushGL()
            renderPartialBar(graphic, xStart + 2, yStart + 2, barWidth)
        } else {
            val secondaryColor = getSecondaryBarColor(index, player)
            secondaryColor.pushGL()
            renderFullBar(graphic, xStart + 2, yStart + 2)
            if (absorption.toInt() % maxHealth.toInt() != 0 && index <= COLORS.size - 1) {
                color.pushGL()
                val width = ((absorption.toInt() % maxHealth.toInt()).toFloat() / maxHealth) * WIDTH
                renderPartialBar(graphic, xStart + 2, yStart + 2, width.toInt())
            }
        }

    }

    override fun renderText(
        graphic: GuiGraphics,
        player: Player,
        screenWidth: Int,
        screenHeight: Int,
        vOffset: Int
    ) {
        val absorption = player.absorptionAmount
        val maxHealth = player.maxHealth

        val xStart = screenWidth / 2 + iconOffset
        val yStart = screenHeight - vOffset

        val index = min(ceil(absorption / maxHealth).toInt(), COLORS.size) - 1
        val color = getPrimaryBarColor(index, player).value

        RGB.reset()
        drawValue(graphic, xStart, yStart, absorption, color)
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

        val isHardcore = player.level().levelData.isHardcore
        val icon = if (isHardcore) HARDCORE_ICON else ICON

        RGB.reset()
        graphic.blitSprite(CONTAINER, xStart, yStart, 9, 9)
        graphic.blitSprite(icon, xStart, yStart, 9, 9)
    }

    override fun getPrimaryBarColor(
        index: Int,
        player: Player
    ): RGB {
        val effect = HealthHud.getHealthEffect(player)
        return when (effect) {
            HealthEffect.NONE -> COLORS[index]
            HealthEffect.POISON -> POISON_COLORS[index]
            HealthEffect.WITHER -> WITHER_COLORS[index]
        }
    }

    override fun getBarWidth(player: Player): Int {
        val absorption = player.absorptionAmount
        val maxHealth = player.maxHealth
        return ceil(WIDTH * min(absorption, maxHealth) / maxHealth).toInt()
    }
}