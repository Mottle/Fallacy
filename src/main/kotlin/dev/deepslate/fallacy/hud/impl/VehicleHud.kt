package dev.deepslate.fallacy.hud.impl

import dev.deepslate.fallacy.hud.HealthEffect
import dev.deepslate.fallacy.util.RGB
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import kotlin.math.ceil

class VehicleHud : HudLayerImpl("vehicle") {

    companion object {
        private val ICON = ResourceLocation.withDefaultNamespace("hud/heart/vehicle_full")

        private val CONTAINER = ResourceLocation.withDefaultNamespace("hud/heart/vehicle_container")
    }

    override var isRightHandSide: Boolean = true

    private var healthUpdateCounter = 0L

    private var mountHealth = 0

    override fun shouldRender(player: Player): Boolean = player.vehicle is LivingEntity

    override fun renderBar(
        graphic: GuiGraphics,
        player: Player,
        screenWidth: Int,
        screenHeight: Int,
        vOffset: Int
    ) {
        val updateCount = Minecraft.getInstance().gui.guiTicks
        val vehicle = player.vehicle as LivingEntity

        if (!vehicle.isAlive) return

        val health = vehicle.health
        val maxHealth = vehicle.maxHealth
        val barWidth = getBarWidth(player)
        val isHighlight = healthUpdateCounter > updateCount && (healthUpdateCounter - updateCount) / 3L % 2L == 1L

        if (health < mountHealth && player.invulnerableTime > 0) {
            healthUpdateCounter = updateCount + 20L
        } else if (health > mountHealth && player.invulnerableTime > 0) {
            healthUpdateCounter = updateCount + 10L
        }

        mountHealth = health.toInt()
        val xStart = screenWidth / 2 + hOffset
        val yStart = screenHeight - vOffset
        val relateY = if (isHighlight) 18 else 0

        graphic.blit(ICON_BAR, xStart, yStart, 0, relateY, 81, 9)
        HealthHud.calculateScaledColor(health, maxHealth, HealthEffect.NONE).pushGL()

        val barFrom = xStart + if (isRightHandSide) WIDTH - barWidth else 0

        renderPartialBar(graphic, barFrom + 2, yStart + 2, barWidth)
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
        val vehicle = player.vehicle as LivingEntity
        val maxHealth = vehicle.maxHealth
        val color = HealthHud.calculateScaledColor(mountHealth.toFloat(), maxHealth, HealthEffect.NONE)

        RGB.reset()
        drawValue(graphic, xStart, yStart, mountHealth, color.value)
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

        graphic.blitSprite(CONTAINER, xStart, yStart, 9, 9)
        graphic.blitSprite(ICON, xStart, yStart, 9, 9)
    }

    override fun getBarWidth(player: Player): Int {
        val vehicle = player.vehicle as LivingEntity
        val health = vehicle.health
        val maxHealth = vehicle.maxHealth
        return ceil(WIDTH * (health / maxHealth)).toInt()
    }
}