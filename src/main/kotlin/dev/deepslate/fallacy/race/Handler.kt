package dev.deepslate.fallacy.race

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.common.network.packet.RaceIdSyncPacket
import dev.deepslate.fallacy.common.network.packet.SelectRacePacket
import dev.deepslate.fallacy.race.impl.Unknown
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

    //client side
    internal fun handleRaceIdSyncPacket(data: RaceIdSyncPacket, context: IPayloadContext) {
        val player = context.player()
        player.setData(FallacyAttachments.RACE_ID, data.raceId)
        Fallacy.LOGGER.info("Syncing race id: ${data.raceId}.")
    }

    //server side
    internal fun handleSelectRacePacket(data: SelectRacePacket, context: IPayloadContext) {
        val raceId = data.raceId
        val race = Races.REGISTRY.get(raceId) ?: Unknown.INSTANCE
        val player = context.player() as ServerPlayer
        Race.setNewRace(player, race)
    }

    @SubscribeEvent
    fun onPlayerLogin(event: PlayerEvent.PlayerLoggedInEvent) {
        val player = event.entity as ServerPlayer
        Race.sync(player)
    }

    @SubscribeEvent
    fun onServerPlayerTick(event: PlayerTickEvent.Post) {
        if (event.entity.level().isClientSide) return
        if (!TickHelper.checkServerTickRate(Race.RACE_TICK_RATE)) return

        val player = event.entity as? ServerPlayer ?: return

        if (!player.isAlive) return

        val race = Race.get(player)
        val level = player.level() as ServerLevel

        race.tick(level, player, player.blockPosition())
    }

    @SubscribeEvent
    fun onServerPlayerRespawn(event: PlayerEvent.PlayerRespawnEvent) {
        if (event.entity.level().isClientSide) return

        val player = event.entity as ServerPlayer
        val race = Race.get(player)

        Race.sync(player)
        if (race !is Respawnable) return
        race.onRespawn(player)
    }

    @SubscribeEvent
    fun onServerPlayerClone(event: PlayerEvent.Clone) {
        if (!event.isWasDeath) return
        if (event.entity.level().isClientSide) return

        val player = event.entity as ServerPlayer
        val original = event.original as ServerPlayer
        val race = Race.get(player)
        if (race !is Respawnable) return
        race.onClone(player, original)
    }
}