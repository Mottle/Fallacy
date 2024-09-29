package dev.deepslate.fallacy.client.hud

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.client.hud.impl.*
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw
import net.minecraft.world.entity.player.Player


class LayerRender : LayeredDraw.Layer {

    companion object {
        private val BARS = listOf(
            HealthHud(), HungerHud(), AbsorptionHud(), HydrationHud(), AirHud(), VehicleHud(),
            ArmorHud()
        )
    }

    override fun render(
        guiGraphics: GuiGraphics,
        deltaTracker: DeltaTracker
    ) {
        val mc = Minecraft.getInstance()
        val entity = mc.getCameraEntity()

        if (entity == null || entity !is Player) return
        if (entity.abilities.instabuild || entity.isSpectator) return

        mc.profiler.push("fallacy_hud")

//        val healthBar = HealthBar()
//        health.rightHandSide = true

        BARS.forEach { bar ->
            if (bar.isErrored) return@forEach
            try {
                bar.render(
                    guiGraphics,
                    entity,
                    guiGraphics.guiWidth(),
                    guiGraphics.guiHeight(),
                    getOffset(guiGraphics, bar.isRightHandSide)
                )
            } catch (e: Exception) {
                Fallacy.LOGGER.error(e)
                bar.isErrored = true
            }
        }

        mc.profiler.pop()
    }

    private fun getOffset(graphic: GuiGraphics, right: Boolean) = with(Minecraft.getInstance().gui) {
        if (right) rightHeight else leftHeight
    }
}




































