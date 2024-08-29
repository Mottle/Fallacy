package dev.deepslate.fallacy.race

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.common.network.packet.RaceIdSyncPacket
import net.minecraft.server.level.ServerPlayer
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext

@EventBusSubscriber(modid = Fallacy.MOD_ID)
object Handler {
    fun handleRaceIdSyncPacket(data: RaceIdSyncPacket, context: IPayloadContext) {
        val player = context.player()
        player.setData(FallacyAttachments.RACE_ID, data.raceId)
        Fallacy.LOGGER.info("Syncing race id: ${data.raceId}.")
    }

    @SubscribeEvent
    fun onPlayerLogin(event: PlayerEvent.PlayerLoggedInEvent) {
        val player = event.entity as ServerPlayer
        val raceId = player.getData(FallacyAttachments.RACE_ID)
        PacketDistributor.sendToPlayer(player, RaceIdSyncPacket(raceId))
    }
}