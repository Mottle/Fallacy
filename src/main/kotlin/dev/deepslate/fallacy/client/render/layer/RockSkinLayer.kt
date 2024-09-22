package dev.deepslate.fallacy.client.render.layer

import com.mojang.blaze3d.vertex.PoseStack
import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.item.FallacyItems
import net.minecraft.client.model.PlayerModel
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.player.Player

class RockSkinLayer(val render: RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>) :
    RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>(render) {

    companion object {
        private val TEXTURE_LOCATION =
            Fallacy.id("textures/item/race/rock_skin.png")
    }


    private fun xOffset(f: Float) = f * 0.001F

    override fun render(
        poseStack: PoseStack,
        bufferSource: MultiBufferSource,
        packedLight: Int,
        livingEntity: AbstractClientPlayer,
        limbSwing: Float,
        limbSwingAmount: Float,
        partialTick: Float,
        ageInTicks: Float,
        netHeadYaw: Float,
        headPitch: Float
    ) {
        if (!checkRockSkin(livingEntity)) return

        val f = livingEntity.tickCount + partialTick
        val entityModel = render.model
        val vertexConsumer = bufferSource.getBuffer(
            RenderType.energySwirl(
                TEXTURE_LOCATION,
                xOffset(f) % 1.0F,
                f * 0.01F % 1.0F
            )
        )
        entityModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, -8355712);
    }

    private fun checkRockSkin(player: Player): Boolean {
        if (player.getItemBySlot(EquipmentSlot.HEAD).`is`(FallacyItems.Race.ROCK_SKIN_HELMET)) return true
        if (player.getItemBySlot(EquipmentSlot.CHEST).`is`(FallacyItems.Race.ROCK_SKIN_CHESTPLATE)) return true
        if (player.getItemBySlot(EquipmentSlot.LEGS).`is`(FallacyItems.Race.ROCK_SKIN_LEGGINGS)) return true
        if (player.getItemBySlot(EquipmentSlot.FEET).`is`(FallacyItems.Race.ROCK_SKIN_BOOTS)) return true
        return false
    }
}