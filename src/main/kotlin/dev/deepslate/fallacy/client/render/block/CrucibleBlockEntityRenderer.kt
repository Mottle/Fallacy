package dev.deepslate.fallacy.client.render.block

import com.mojang.blaze3d.vertex.PoseStack
import dev.deepslate.fallacy.common.block.entity.CrucibleEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.renderer.LightTexture
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.HitResult
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.FluidStack

class CrucibleBlockEntityRenderer(val context: BlockEntityRendererProvider.Context) :
    BlockEntityRenderer<CrucibleEntity> {
    override fun render(
        blockEntity: CrucibleEntity,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        // 获取玩家看向的方块
        val camera = Minecraft.getInstance().cameraEntity ?: return
        val hitRes = camera.pick(5.0, 0f, false)
        if (hitRes.type != HitResult.Type.BLOCK) return
        val blockHitRes = hitRes as BlockHitResult
        val lookAt = blockHitRes.blockPos
        val looked = blockEntity.level?.getBlockState(lookAt) ?: return
        if (blockEntity.blockState != looked) return
        // 获取玩家看向的方块 -- end

        val inventory = blockEntity.inventory.filter { !it.isEmpty }
        val itemRenderer = context.itemRenderer

        poseStack.pushPose()
        poseStack.translate(0.5, 1.5, 0.5) // 调整高度
        poseStack.mulPose(Minecraft.getInstance().entityRenderDispatcher.cameraOrientation())
        poseStack.scale(0.5f, 0.5f, 0.5f)


        val offset = 0.5f
//        val start = -2f
        val start = -offset * (inventory.size - 1) / 2f
        inventory.forEachIndexed { idx, stack ->
            poseStack.pushPose()
            poseStack.translate(start + offset * idx, 0f, 0f)

            itemRenderer.render(
                stack,
                ItemDisplayContext.GROUND,
                false,
                poseStack,
                bufferSource,
                packedLight,
                packedOverlay,
                itemRenderer.getModel(stack, blockEntity.level, null, 0)
            )

            poseStack.popPose()
        }

        if (!blockEntity.isTankEmpty()) {
            try {
                renderFluid(blockEntity.cachedFluid, poseStack, bufferSource, packedLight)
            } finally {
            }
        }

        poseStack.popPose()
    }

    private fun renderFluid(
        fluidStack: FluidStack,
        poseStack: PoseStack,
        buffers: MultiBufferSource,
        packedLight: Int
    ) {
        poseStack.translate(0f, -0.5f, 0f)
//        renderFluidFace(fluidStack, poseStack, buffers)
//        poseStack.translate(0.5f, -1.5f, 0.5f)
//        poseStack.pushPose()
//        context.font.drawInBatch(
//            fluidStack.amount.toString(),
//            -context.font.width(fluidStack.amount.toString()).toFloat() / 2f,
//            0f,
//            0xffffff,
//            false,
//            poseStack.last().pose(),
//            buffers,
//            Font.DisplayMode.SEE_THROUGH,
//            0,
//            packedLight
//        )
//        poseStack.popPose()

        poseStack.run {
//            translate(0.5, 1.5, 0.5) // 调整高度
            scale(0.025f, -0.025f, 0.025f)

            val font = context.font
            val textWidth = font.width("text").toFloat()
            val matrix = last().pose()

            font.drawInBatch(
                "text",
                -textWidth / 2f,
                0f,
                0xffffff,
                false,
                matrix,
                buffers,
                Font.DisplayMode.SEE_THROUGH,
                0,
                packedLight
            )
        }
    }

    private fun renderFluidFace(fluidStack: FluidStack, poseStack: PoseStack, buffers: MultiBufferSource) {
        val renderProps = IClientFluidTypeExtensions.of(fluidStack.fluid)
        val texture = renderProps.getStillTexture(fluidStack);
        val color = renderProps.getTintColor(fluidStack);
        val sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
            .apply(texture);

        poseStack.pushPose()
        // Push it out of the block face a bit to avoid z-fighting
//        poseStack.translate(0f, 0f, 0.01f);

        val buffer = buffers.getBuffer(RenderType.solid())

        // In comparison to items, make it _slightly_ smaller because item icons
        // usually don't extend to the full size.
        val scale = 0.5f

        // y is flipped here
        val x0 = -scale / 2
        val y0 = scale / 2
        val x1 = scale / 2
        val y1 = -scale / 2

        val combinedLight = LightTexture.FULL_BRIGHT
        val transform = poseStack.last().pose()
        buffer.addVertex(transform, x0, y1, 0f)
            .setColor(color)
            .setUv(sprite.u0, sprite.v1)
            .setOverlay(OverlayTexture.NO_OVERLAY)
            .setLight(combinedLight)
            .setNormal(0f, 0f, 1f)
        buffer.addVertex(transform, x1, y1, 0f)
            .setColor(color)
            .setUv(sprite.u1, sprite.v1)
            .setOverlay(OverlayTexture.NO_OVERLAY)
            .setLight(combinedLight)
            .setNormal(0f, 0f, 1f)
        buffer.addVertex(transform, x1, y0, 0f)
            .setColor(color)
            .setUv(sprite.u1, sprite.v0)
            .setOverlay(OverlayTexture.NO_OVERLAY)
            .setLight(combinedLight)
            .setNormal(0f, 0f, 1f)
        buffer.addVertex(transform, x0, y0, 0f)
            .setColor(color)
            .setUv(sprite.u0, sprite.v0)
            .setOverlay(OverlayTexture.NO_OVERLAY)
            .setLight(combinedLight)
            .setNormal(0f, 0f, 1f)
        poseStack.popPose()
    }
}