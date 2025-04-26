package dev.deepslate.fallacy.client.render

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import dev.deepslate.fallacy.util.TickHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.LevelRenderer
import net.minecraft.client.renderer.RenderType
import net.minecraft.core.BlockPos
import net.minecraft.core.Vec3i
import net.minecraft.world.phys.AABB
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RenderLevelStageEvent
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.toVec3

@OnlyIn(Dist.CLIENT)
object LineBoxRenderer {
    private val boxes = mutableListOf<BoxRenderContext>()

    fun add(origin: BlockPos, size: Vec3i, ticks: Int = TickHelper.second(5)) {
        boxes += BoxRenderContext(origin, size, ticks)
    }

    private data class BoxRenderContext(val origin: BlockPos, val size: Vec3i, var remainTicks: Int) {
        fun tick() {
            remainTicks--
        }

        val alive = remainTicks > 0
    }

    @EventBusSubscriber
    object Handler {
        @SubscribeEvent
        fun onWorldRender(event: RenderLevelStageEvent) {
            if (event.stage != RenderLevelStageEvent.Stage.AFTER_LEVEL) return

            val minecraft = Minecraft.getInstance()
            val pose = event.poseStack
            val buffer = minecraft.renderBuffers().bufferSource().getBuffer(RenderType.lines())

            pose.pushPose()
            boxes.forEach { context ->
                if (!context.alive) {
                    boxes.remove(context)
                    return@forEach
                }

                context.tick()
                render(pose, buffer, context.origin, context.size)
            }
            pose.popPose()
        }

        private fun render(pose: PoseStack, buffer: VertexConsumer, origin: BlockPos, size: Vec3i) {
            val aabb = AABB(origin.toVec3(), origin.offset(size).toVec3())
            LevelRenderer.renderLineBox(pose, buffer, aabb, 1.0f, 0.0f, 0.0f, 1.0f)
        }
    }
}