package dev.deepslate.fallacy.client.hud.impl

import com.mojang.blaze3d.systems.RenderSystem
import dev.deepslate.fallacy.race.Race
import dev.deepslate.fallacy.race.impl.Skeleton
import dev.deepslate.fallacy.util.RGB
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.player.Player

class HealthHud : HudLayerImpl("health") {

    companion object {

        private val NORMAL_COLORS = listOf("#FF0000", "#FFFF00", "#00FF00").map(RGB::fromHex)

        private val POISON_COLORS = listOf("#00FF00", "#55FF55", "#00FF00").map(RGB::fromHex)

        private val WITHER_COLORS = listOf("#555555", "#AAAAAA", "#555555").map(RGB::fromHex)

        private val NORMAL_ICON = ResourceLocation.withDefaultNamespace("hud/heart/full")

        private val POISON_ICON = ResourceLocation.withDefaultNamespace("hud/heart/poisoned_full")

        private val WITHER_ICON = ResourceLocation.withDefaultNamespace("hud/heart/withered_full")

        private val HARDCORE_ICON = ResourceLocation.withDefaultNamespace("hud/heart/hardcore_full")

        private val HARDCORE_POISON_ICON = ResourceLocation.withDefaultNamespace("hud/heart/poisoned_hardcore_full")

        private val HARDCORE_WITHER_ICON = ResourceLocation.withDefaultNamespace("hud/heart/withered_hardcore_full")

        private val CONTAINER = ResourceLocation.withDefaultNamespace("hud/heart/container")

        internal fun calculateScaledColor(
            d1: Float,
            d2: Float,
            effect: dev.deepslate.fallacy.client.hud.HealthEffect
        ): RGB {
            val (colors, fractions) = when (effect) {
                dev.deepslate.fallacy.client.hud.HealthEffect.NONE -> Pair(NORMAL_COLORS, listOf(.25f, .5f, .75f))
                dev.deepslate.fallacy.client.hud.HealthEffect.POISON -> Pair(POISON_COLORS, listOf(.25f, .5f, .75f))
                dev.deepslate.fallacy.client.hud.HealthEffect.WITHER -> Pair(WITHER_COLORS, listOf(.25f, .5f, .75f))
            }

            val rate = d1 / d2

            val index = fractions.indexOfFirst { f -> f >= rate }
            if (index == -1) return colors.last()
            if (index == 0) return colors.first()

            val colorFrom = colors[index - 1]
            val colorTo = colors[index]
            val frac = index - fractions[index - 1] / (fractions[index] - fractions[index - 1])

            return colorFrom.blend(colorTo, frac)
        }

        fun getHealthEffect(player: Player): dev.deepslate.fallacy.client.hud.HealthEffect {
            if (player.hasEffect(MobEffects.POISON)) return dev.deepslate.fallacy.client.hud.HealthEffect.POISON
            if (player.hasEffect(MobEffects.WITHER)) return dev.deepslate.fallacy.client.hud.HealthEffect.WITHER
            return dev.deepslate.fallacy.client.hud.HealthEffect.NONE
        }
    }

    //玩家不为Skeleton时显示
    override fun shouldRender(player: Player): Boolean = Race.get(player) !is Skeleton

    private var playerHealth = 0f

    private var lastPlayerHealth = 0f

    private var healthUpdateCounter = 0L

    override fun renderBar(
        graphic: GuiGraphics,
        player: Player,
        screenWidth: Int,
        screenHeight: Int,
        vOffset: Int
    ) {
        val updateCounter = Minecraft.getInstance().gui.guiTicks
        val health = player.health
        val barWith = getBarWidth(player)
        val isHighlight =
            healthUpdateCounter > updateCounter && (healthUpdateCounter - updateCounter) / 3 % 2 == 1.toLong()

        if (health < playerHealth && player.invulnerableTime > 0) {
            healthUpdateCounter = updateCounter + 20L
            lastPlayerHealth = playerHealth
        } else if (health > playerHealth && player.invulnerableTime > 0) {
            healthUpdateCounter = updateCounter + 10L
        }
        playerHealth = health

        val healthDeltaRate = with(player) {
            invulnerableTime.toFloat() / invulnerableDuration.toFloat()
        }
        val healthDelta = (lastPlayerHealth - health) * healthDeltaRate
        val displayHealth = health + healthDelta
        val xStart = screenWidth / 2 + hOffset
        val yStart = screenHeight - vOffset
        val maxHealth = player.maxHealth
        val relateY = if (isHighlight) 18 else 0

        RGB.reset()

        graphic.blit(ICON_BAR, xStart, yStart, 0, relateY, WIDTH + 4, 9)

        val barFromPos = xStart + if (isRightHandSide) WIDTH - barWith else 0

        //血条缓冲帧
        if (displayHealth > health) {
            RGB.reset()
            val w = (displayHealth / maxHealth) * WIDTH
            val off = if (isRightHandSide) w - barWith else 0f
            renderPartialBar(graphic, (barFromPos + 2 - off).toInt(), yStart + 2, w.toInt())
        }

        val primaryColor = getPrimaryBarColor(0, player)
        primaryColor.pushGL()
        renderPartialBar(graphic, barFromPos.toInt() + 2, yStart + 2, barWith)

        if (getHealthEffect(player) == dev.deepslate.fallacy.client.hud.HealthEffect.POISON) {
            RenderSystem.setShaderColor(0f, .5f, 0f, .5f)
            graphic.blit(ICON_BAR, barFromPos.toInt() + 1, yStart + 1, 1, 36, barWith, 7)
        }
    }

    override fun renderText(
        graphic: GuiGraphics,
        player: Player,
        screenWidth: Int,
        screenHeight: Int,
        vOffset: Int
    ) {
        val health = player.health
        val xStart = screenWidth / 2 + iconOffset
        val yStart = screenHeight - vOffset
        drawValue(graphic, xStart, yStart, health, getPrimaryBarColor(0, player).value)
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
        val effect = getHealthEffect(player)

        val icon = if (isHardcore) when (effect) {
            dev.deepslate.fallacy.client.hud.HealthEffect.NONE -> HARDCORE_ICON
            dev.deepslate.fallacy.client.hud.HealthEffect.POISON -> HARDCORE_POISON_ICON
            dev.deepslate.fallacy.client.hud.HealthEffect.WITHER -> HARDCORE_WITHER_ICON
        } else when (effect) {
            dev.deepslate.fallacy.client.hud.HealthEffect.NONE -> NORMAL_ICON
            dev.deepslate.fallacy.client.hud.HealthEffect.POISON -> POISON_ICON
            dev.deepslate.fallacy.client.hud.HealthEffect.WITHER -> WITHER_ICON
        }

        RGB.reset()
        graphic.blitSprite(CONTAINER, xStart, yStart, 9, 9)
        graphic.blitSprite(icon, xStart, yStart, 9, 9)
    }

    override fun getBarWidth(player: Player): Int {
        val health = player.health
        val maxHealth = player.maxHealth

        return (WIDTH * health / maxHealth).toInt()
    }

    override fun getPrimaryBarColor(
        index: Int,
        player: Player
    ): RGB {
        val health = player.health
        val maxHealth = player.maxHealth
        val effect = getHealthEffect(player)
        return calculateScaledColor(health, maxHealth, effect)
    }
}