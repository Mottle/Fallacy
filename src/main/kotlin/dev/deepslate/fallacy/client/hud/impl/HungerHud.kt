package dev.deepslate.fallacy.client.hud.impl

import com.mojang.blaze3d.systems.RenderSystem
import dev.deepslate.fallacy.common.data.FallacyAttributes
import dev.deepslate.fallacy.util.RGB
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.core.component.DataComponents
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.player.Player
import java.lang.Math.clamp
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.sin

class HungerHud : HudLayerImpl("hunger") {

    companion object {

        private val HUNGER_ICON = ResourceLocation.withDefaultNamespace("hud/food_full")

        private val HUNGER_DEBUFF_ICON = ResourceLocation.withDefaultNamespace("hud/food_full_hunger")

        private val CONTAINER = ResourceLocation.withDefaultNamespace("hud/food_empty")

        private val HUNGER_COLOR = RGB.fromHex("#B34D00")

        private val HUNGER_DEBUFF_COLOR = RGB.fromHex("#249016")

        private val SATURATION_COLOR = RGB.fromHex("#FFCC00")

        private val SATURATION_DEBUFF_COLOR = RGB.fromHex("#87BC00")
    }

    override var isRightHandSide: Boolean = true

    private fun getMaxHunger() =
        Minecraft.getInstance().player?.getAttributeValue(FallacyAttributes.MAX_HUNGER)?.toInt() ?: 20

    override fun renderBar(
        graphic: GuiGraphics,
        player: Player,
        screenWidth: Int,
        screenHeight: Int,
        vOffset: Int
    ) {
        val hunger = player.foodData.foodLevel
        val maxHunger = getMaxHunger()
        val saturation = player.foodData.saturationLevel
        val maxSaturation = 20f
        val barWidthH = getBarWidth(player)
        val barWidthS = getSaturationWidth(player)
        val exhaustion = player.foodData.exhaustionLevel

        val xStart = screenWidth / 2 + hOffset
        val yStart = screenHeight - vOffset

        RGB.reset()
        renderFullBackground(graphic, xStart, yStart)

        val barFrom = xStart + if (isRightHandSide) WIDTH - barWidthH else 0
        val hungerColor = getSecondaryBarColor(0, player)
        val saturationColor = getPrimaryBarColor(0, player)

        hungerColor.pushGL()
        renderPartialBar(graphic, barFrom + 2, yStart + 2, barWidthH)

        if (saturation > 0) {
            saturationColor.pushGL()
            val barFrom = xStart + if (isRightHandSide) WIDTH - barWidthS else 0
            renderPartialBar(graphic, barFrom + 2, yStart + 2, barWidthS)
        }

        //绘制变化条
        if (player.mainHandItem.has(DataComponents.FOOD)) {
            val foodSetting = player.mainHandItem.get(DataComponents.FOOD)!!
            val time = (System.currentTimeMillis() / 1000.0) * 3.0
            val alpha = (sin(time) / 2.0 + 0.5).toFloat()

            val hungerWidth = min(maxHunger - hunger, foodSetting.nutrition)

            RenderSystem.enableBlend()

            if (hunger < maxHunger) {
                val width = WIDTH * ((hungerWidth + hunger).toFloat() / maxHunger.toFloat())
                val barFrom = xStart + if (isRightHandSide) WIDTH - width else 0f

                hungerColor.pushGLWithAlpha(alpha)
                renderPartialBar(graphic, barFrom.toInt() + 2, yStart + 2, width.toInt())
            }

//            val saturationW = listOf(
//                potentialSaturation,
//                maxSaturation - saturation,
//                (hunger + hungerWidth).toFloat(),
//                (hungerOverlay + hunger).toFloat()
//            ).min()
//            val finalSat =
//                if ((potentialSaturation + saturation) > (hunger + hungerWidth)) {
//                    val diff = (potentialSaturation + saturation) - (hunger + hungerWidth)
//                    potentialSaturation - diff
//                } else saturationW
            val finalSat = clamp(
                foodSetting.nutrition + saturation,
                0f,
                (hungerWidth + hunger).toFloat()
            )

            val width = (WIDTH * finalSat / maxSaturation).toInt()
            val barFrom = xStart + if (isRightHandSide) WIDTH - width else 0

            saturationColor.pushGLWithAlpha(alpha)
            renderPartialBar(graphic, barFrom + 2, yStart + 2, width)

            RenderSystem.disableBlend()
        }

        val finalExhaustion = min(exhaustion, 4f)
        val exhaustionWidth = (WIDTH * (finalExhaustion / 4f))
        val f = xStart + if (isRightHandSide) WIDTH - exhaustionWidth else 0f
        RenderSystem.setShaderColor(1f, 1f, 1f, .25f)
        val barWidth = (WIDTH * (finalExhaustion / 4f)).toInt()
        graphic.blit(ICON_BAR, f.toInt() + 2, yStart + 1, 1, 28, barWidth, 9)
    }

    override fun renderText(
        graphic: GuiGraphics,
        player: Player,
        screenWidth: Int,
        screenHeight: Int,
        vOffset: Int
    ) {
        RGB.reset()

        val xStart = screenWidth / 2 + iconOffset
        val yStart = screenHeight - vOffset
        val hunger = player.foodData.foodLevel
        val color = getSecondaryBarColor(0, player).value

        drawValue(graphic, xStart, yStart, hunger, color)
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
        val isHungerDebuff = player.hasEffect(MobEffects.HUNGER)
        val icon = if (isHungerDebuff) HUNGER_DEBUFF_ICON else HUNGER_ICON

        graphic.blitSprite(CONTAINER, xStart, yStart, 9, 9)
        graphic.blitSprite(icon, xStart, yStart, 9, 9)
    }

    override fun getBarWidth(player: Player): Int {
        val hunger = player.foodData.foodLevel
        val maxHunger = getMaxHunger().toFloat()
        return ceil(hunger / maxHunger * WIDTH).toInt()
    }

    fun getSaturationWidth(player: Player): Int {
        val saturation = player.foodData.saturationLevel
        val maxSaturation = getMaxHunger()
        return ceil(saturation / maxSaturation * WIDTH).toInt()
    }

    override fun getPrimaryBarColor(
        index: Int,
        player: Player
    ): RGB = if (player.hasEffect(MobEffects.HUNGER)) SATURATION_DEBUFF_COLOR else SATURATION_COLOR

    override fun getSecondaryBarColor(
        index: Int,
        player: Player
    ): RGB = if (player.hasEffect(MobEffects.HUNGER)) HUNGER_DEBUFF_COLOR else HUNGER_COLOR
}