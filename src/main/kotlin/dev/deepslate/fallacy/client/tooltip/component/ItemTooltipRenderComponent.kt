package dev.deepslate.fallacy.client.tooltip.component

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent

class ItemTooltipRenderComponent(val component: ItemTooltipComponent) : ClientTooltipComponent {

    override fun getHeight(): Int = 16

    override fun getWidth(font: Font): Int =
        font.width(component.preString ?: "") + 18 + (component.postString?.let { font.width(it) } ?: 0)

    override fun renderImage(font: Font, x: Int, y: Int, guiGraphics: GuiGraphics) {
        val defaultStack = component.item.defaultInstance
        val preStringWidth = font.width(component.preString ?: "")

        component.preString?.let { guiGraphics.drawString(font, it, x, y + 4, 0xffffff) }
        guiGraphics.renderItem(defaultStack, x + preStringWidth, y)
        guiGraphics.renderItemDecorations(font, defaultStack, x + preStringWidth, y)
        component.postString?.let { guiGraphics.drawString(font, it, x + preStringWidth + 18, y + 4, 0xffffff) }
    }
}