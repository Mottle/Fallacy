package dev.deepslate.fallacy.client.render.block

import com.mojang.blaze3d.vertex.PoseStack
import dev.deepslate.fallacy.common.block.entity.TickDurationBlockEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class TickDurationBlockEntityRenderer<T : TickDurationBlockEntity>(val context: BlockEntityRendererProvider.Context) :
    BlockEntityRenderer<T> {
    override fun render(
        blockEntity: T,
        partialTick: Float,
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        packedOverlay: Int
    ) {
        val text = blockEntity.durationTicks.toString()

        poseStack.run {
            pushPose()
            translate(0.5, 1.5,  0.5) // 调整高度
            mulPose(Minecraft.getInstance().entityRenderDispatcher.cameraOrientation())
//            scale(-0.02f, -0.02f, 0.02f) // 调整大小
            scale(0.025f, -0.025f, 0.025f)

            val font = context.font
            val textWidth = font.width(text).toFloat()
            val matrix = last().pose()

            font.drawInBatch(
                text,
                -textWidth / 2f,
                0f,
                0xffffff,
                false,
                matrix,
                bufferSource,
                Font.DisplayMode.SEE_THROUGH,
                0,
                packedLight
            )

            popPose()
        }

//        poseStack.pushPose()
//        poseStack.translate(0.5, 1.5, 0.5)
//        poseStack.mulPose(Minecraft.getInstance().entityRenderDispatcher.cameraOrientation())
//        poseStack.scale(-0.2f, -0.2f, 0.2f)
//        Minecraft.getInstance().itemRenderer.renderStatic(
//            ItemStack(Items.DIAMOND),
//            ItemDisplayContext.GROUND,
//            packedLight,
//            packedOverlay,
//            poseStack,
//            bufferSource,
//            blockEntity.getLevel(),
//            0
//        )
//        poseStack.popPose()
    }
}