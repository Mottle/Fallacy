package dev.deepslate.fallacy.client.hud

import dev.deepslate.fallacy.client.hud.impl.*
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.LayeredDraw
import net.minecraft.world.entity.player.Player
import org.apache.logging.log4j.LogManager


class LayerRenderer : LayeredDraw.Layer {

    companion object {
        private val BARS = listOf(
            HealthHud(), BoneHud(), HungerHud(), AbsorptionHud(), HydrationHud(), AirHud(), VehicleHud(),
            ArmorHud()
        )

        private val logger = LogManager.getLogger("fallacy-layerRenderer")
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
                logger.error(e)
                bar.isErrored = true
            }
        }

        mc.profiler.pop()
//        Fallacy.LOGGER.info(mc.player?.getData(FallacyAttachments.RACE_ID))
    }

    private fun getOffset(graphic: GuiGraphics, right: Boolean) = with(Minecraft.getInstance().gui) {
        if (right) rightHeight else leftHeight
    }
}




































