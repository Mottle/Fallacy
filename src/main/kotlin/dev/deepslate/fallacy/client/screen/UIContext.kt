package dev.deepslate.fallacy.client.screen

import dev.deepslate.fallacy.client.screen.component.ExtendedUIRender
import net.minecraft.client.Minecraft
import com.github.wintersteve25.tau.components.base.UIComponent as TauUIComponent
import net.minecraft.client.gui.screens.Screen as MinecraftScreen


data class UIContext(val visited: List<VisitedUI> = emptyList()) {
    sealed class VisitedUI {

        abstract fun display()

        data class Screen(val screen: MinecraftScreen) : VisitedUI() {
            override fun display() {
                Minecraft.getInstance().setScreen(screen)
            }
        }

        data class UIComponent(val uiComponent: TauUIComponent) : VisitedUI() {
            override fun display() {
                Minecraft.getInstance().setScreen(ExtendedUIRender(uiComponent))
            }
        }
    }

    fun previousUI() = visited.first()

    fun displayPreviousUI() {
        previousUI().display()
    }

    fun generateNext(uiComponent: TauUIComponent) = UIContext(visited + VisitedUI.UIComponent(uiComponent))

    fun generateNext(screen: MinecraftScreen) = UIContext(visited + VisitedUI.Screen(screen))
}