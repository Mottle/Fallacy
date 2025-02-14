package dev.deepslate.fallacy.bodyheat

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.capability.FallacyCapabilities
import dev.deepslate.fallacy.common.capability.Synchronous
import dev.deepslate.fallacy.common.network.packet.BodyHeatSyncPacket
import net.minecraft.server.level.ServerPlayer
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.network.handling.IPayloadContext

@EventBusSubscriber(modid = Fallacy.MOD_ID)
object Handler {
    @SubscribeEvent
    fun onPlayerLogin(event: PlayerEvent.PlayerLoggedInEvent) {
        val player = event.entity as ServerPlayer
        val capability = player.getCapability(FallacyCapabilities.BODY_HEAT)

        if (capability is Synchronous) {
            capability.synchronize()
        }
    }

    //client
    fun handleSync(data: BodyHeatSyncPacket, context: IPayloadContext) {
        val bodyHeat = data.value
        val player = context.player()
        val capability = player.getCapability(FallacyCapabilities.BODY_HEAT)
        capability!!.heat = bodyHeat
    }
}