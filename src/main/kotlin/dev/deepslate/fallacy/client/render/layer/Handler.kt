package dev.deepslate.fallacy.client.render.layer

import dev.deepslate.fallacy.Fallacy
import net.minecraft.client.model.HumanoidArmorModel
import net.minecraft.client.model.PlayerModel
import net.minecraft.client.model.geom.LayerDefinitions.OUTER_ARMOR_DEFORMATION
import net.minecraft.client.model.geom.ModelLayers
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.renderer.entity.LivingEntityRenderer
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Player
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.EntityRenderersEvent

//@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = Fallacy.MOD_ID, value = [Dist.CLIENT])
class Handler {
//    @SubscribeEvent
//    fun onDefineLayer(event: EntityRenderersEvent.RegisterLayerDefinitions) {
//        val def = LayerDefinition.create(HumanoidArmorModel.createBodyLayer(OUTER_ARMOR_DEFORMATION), 64, 32)
//        event.registerLayerDefinition(ModelLayers.PLAYER) { def }
//    }

//    @SubscribeEvent
//    fun onRegisterRender(event: EntityRenderersEvent.RegisterRenderers) {
//        event.
//    }
//
//    @SubscribeEvent
//    fun onAddLayer(event: EntityRenderersEvent.AddLayers) {
//        val render = event.getRenderer(EntityType.PLAYER) ?: return as LivingEntityRenderer<Player, PlayerModel<Player>>
//        val layer = RockSkinLayer(render)
//        render.addLayer(layer)
//    }
}