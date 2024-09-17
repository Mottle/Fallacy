package dev.deepslate.fallacy.common.network

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.network.packet.CladdingPacket
import dev.deepslate.fallacy.common.network.packet.DrinkInWorldPacket
import dev.deepslate.fallacy.common.network.packet.RaceIdSyncPacket
import dev.deepslate.fallacy.common.network.packet.ThirstSyncPacket
import dev.deepslate.fallacy.race.impl.Rock
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import dev.deepslate.fallacy.hydration.Handler as HydrationHandler
import dev.deepslate.fallacy.race.Handler as RaceHandler

@EventBusSubscriber(modid = Fallacy.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object FallacyNetwork {

    const val NETWORK_VERSION = "MONKEY"

    @SubscribeEvent
    fun register(event: RegisterPayloadHandlersEvent) {
        val registrar = event.registrar(NETWORK_VERSION)

        registrar.playToClient(
            ThirstSyncPacket.TYPE,
            ThirstSyncPacket.STREAM_CODEC,
            HydrationHandler::handleThirstSyncPacket
        )

        registrar.playToServer(
            DrinkInWorldPacket.TYPE,
            DrinkInWorldPacket.STREAM_CODEC,
            HydrationHandler::handleDrinkInWorldPacket
        )

        registrar.playToClient(
            RaceIdSyncPacket.TYPE,
            RaceIdSyncPacket.STREAM_CODEC,
            RaceHandler::handleRaceIdSyncPacket
        )

        registrar.playToServer(
            CladdingPacket.TYPE,
            CladdingPacket.STREAM_CODEC,
            Rock.Handler::handleCladdingPacket
        )
    }
}