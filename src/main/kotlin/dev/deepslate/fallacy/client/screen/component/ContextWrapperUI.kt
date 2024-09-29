package dev.deepslate.fallacy.client.screen.component

import com.github.wintersteve25.tau.components.base.UIComponent
import com.github.wintersteve25.tau.layout.Layout
import com.github.wintersteve25.tau.theme.Theme
import dev.deepslate.fallacy.client.screen.UIContext

abstract class ContextWrapperUI() : UIComponent {
    private var context = UIContext(emptyList())

    final override fun build(layout: Layout, theme: Theme): UIComponent = build(context, layout, theme)

    fun withContext(context: UIContext): ContextWrapperUI {
        this.context = context
        return this
    }

    abstract fun build(context: UIContext, layout: Layout, theme: Theme): UIComponent
}