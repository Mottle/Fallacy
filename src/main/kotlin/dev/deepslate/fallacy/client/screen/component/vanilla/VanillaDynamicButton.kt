package dev.deepslate.fallacy.client.screen.component.vanilla

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.ImageButton
import net.minecraft.client.gui.components.WidgetSprites
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import javax.annotation.Nonnull


class VanillaDynamicButton(
    private val containerScreen: AbstractContainerScreen<*>, xIn: Int, yIn: Int, widthIn: Int,
    heightIn: Int, sprites: WidgetSprites, onPressIn: OnPress
) : ImageButton(xIn, yIn, widthIn, heightIn, sprites, onPressIn) {

    override fun renderWidget(
        @Nonnull guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int,
        partialTicks: Float
    ) {
        this.x = containerScreen.guiLeft + 126
        this.y = containerScreen.guiTop - 22 + 83
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTicks)
    }
}