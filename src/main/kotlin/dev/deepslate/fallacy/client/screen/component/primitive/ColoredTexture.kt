package dev.deepslate.fallacy.client.screen.component.primitive

import com.github.wintersteve25.tau.build.BuildContext
import com.github.wintersteve25.tau.components.base.PrimitiveUIComponent
import com.github.wintersteve25.tau.components.base.UIComponent
import com.github.wintersteve25.tau.layout.Layout
import com.github.wintersteve25.tau.theme.Theme
import com.github.wintersteve25.tau.utils.SimpleVec2i
import dev.deepslate.fallacy.util.RGB
import net.minecraft.resources.ResourceLocation

class ColoredTexture(
    private val textureLocation: ResourceLocation,
    private val textureSize: SimpleVec2i,
    private val uv: SimpleVec2i,
    private val uvSize: SimpleVec2i,
    private val size: SimpleVec2i,
    private val color: RGB?
) : PrimitiveUIComponent {

    override fun build(layout: Layout, theme: Theme, context: BuildContext): SimpleVec2i {
        val position = layout.getPosition(this.size)
        context.renderables().add { graphics, pMouseX, pMouseY, pPartialTicks ->
            color?.pushGL()
            graphics.blit(
                this.textureLocation,
                position.x,
                position.y,
                this.size.x,
                this.size.y,
                this.uv.x.toFloat(),
                this.uv.y.toFloat(),
                this.uvSize.x,
                this.uvSize.y,
                this.textureSize.x,
                this.textureSize.y
            )
            if (color != null) RGB.reset()
        }
        return this.size
    }

    class Builder(private var textureLocation: ResourceLocation) : UIComponent {
        private var textureSize: SimpleVec2i? = null
        private var uv: SimpleVec2i? = null
        private var uvSize: SimpleVec2i? = null
        private var size: SimpleVec2i? = null
        private var color: RGB? = null

        fun withTextureSize(textureSize: SimpleVec2i) = apply { this.textureSize = textureSize }

        fun withUv(uv: SimpleVec2i) = apply { this.uv = uv }

        fun withUvSize(uvSize: SimpleVec2i) = apply { this.uvSize = uvSize }

        fun withSize(size: SimpleVec2i) = apply { this.size = size }

        fun withColor(color: RGB) = apply { this.color = color }

        fun build() = ColoredTexture(
            this.textureLocation,
            this.textureSize ?: SimpleVec2i(256, 256),
            this.uv ?: SimpleVec2i.zero(),
            this.uvSize ?: this.textureSize ?: SimpleVec2i(256, 256),
            this.size ?: this.uvSize ?: this.textureSize ?: SimpleVec2i(256, 256),
            this.color
        )

        override fun build(layout: Layout, theme: Theme) = build()
    }
}