package dev.deepslate.fallacy.race

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.common.network.packet.RaceIdSyncPacket
import dev.deepslate.fallacy.util.TickHelper
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.tick.PlayerTickEvent
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
        Race.sync(player)
    }

    private const val TICK_RATE = 40

    @SubscribeEvent
    fun onServerPlayerTick(event: PlayerTickEvent.Post) {
        if (!TickHelper.checkServerTickRate(TICK_RATE)) return

        val player = event.entity as? ServerPlayer ?: return

        if (!player.isAlive) return

        val race = Race.get(player)
        val level = player.level() as ServerLevel

        race.tick(level, player, player.blockPosition())
    }
}