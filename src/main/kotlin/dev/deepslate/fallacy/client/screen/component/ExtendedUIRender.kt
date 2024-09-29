package dev.deepslate.fallacy.client.screen.component

import com.github.wintersteve25.tau.components.base.UIComponent
import com.github.wintersteve25.tau.renderer.ScreenUIRenderer

class ExtendedUIRender(uiComponent: UIComponent, renderBackground: Boolean = true, val isPause: Boolean = true) :
    ScreenUIRenderer(uiComponent, renderBackground) {
    override fun isPauseScreen(): Boolean = isPause
}