package dev.deepslate.fallacy.client.particle

import com.mojang.blaze3d.systems.RenderSystem
import com.mojang.blaze3d.vertex.BufferBuilder
import com.mojang.blaze3d.vertex.DefaultVertexFormat
import com.mojang.blaze3d.vertex.Tesselator
import com.mojang.blaze3d.vertex.VertexFormat
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.world.inventory.InventoryMenu

// 定义粒子渲染类型
object ParticleRenderTypes {
    val SORTED_TRANSLUCENT = object : ParticleRenderType {
        override fun begin(
            tesselator: Tesselator,
            textureManager: TextureManager
        ): BufferBuilder? {
            RenderSystem.disableCull()
            return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT.begin(tesselator, textureManager)
        }
    }

    val SORTED_OPAQUE_BLOCK = object : ParticleRenderType {
        override fun begin(
            tesselator: Tesselator,
            textureManager: TextureManager
        ): BufferBuilder? {
            RenderSystem.disableBlend()
            RenderSystem.depthMask(true)
            RenderSystem.setShader(GameRenderer::getParticleShader)
            RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS)

            return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE)
        }

        override fun isTranslucent(): Boolean = false
    }
}