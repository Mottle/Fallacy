package dev.deepslate.fallacy.common.network

import dev.deepslate.fallacy.Calendar
import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.player.FoodHistory
import dev.deepslate.fallacy.common.data.player.NutritionState
import dev.deepslate.fallacy.common.network.packet.*
import dev.deepslate.fallacy.race.impl.Skeleton
import dev.deepslate.fallacy.weather.impl.ClientWeatherEngine
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent
import dev.deepslate.fallacy.common.capability.heat.Handler as BodyHeatHandler
import dev.deepslate.fallacy.hydration.Handler as HydrationHandler
import dev.deepslate.fallacy.race.Handler as RaceHandler
import dev.deepslate.fallacy.race.impl.rock.Handler as RockHandler

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
            RockHandler::handleCladdingPacket
        )

        registrar.playToClient(
            NutritionStateSyncPacket.TYPE,
            NutritionStateSyncPacket.STREAM_CODEC,
            NutritionState.Handler::handleSync
        )

        registrar.playToClient(
            FoodHistorySyncPacket.TYPE,
            FoodHistorySyncPacket.STREAM_CODEC,
            FoodHistory.Handler::handleSync
        )

        registrar.playToClient(
            BoneSyncPacket.TYPE,
            BoneSyncPacket.STREAM_CODEC,
            Skeleton.Handler::handleBoneSync
        )

        registrar.playToServer(
            SelectRacePacket.TYPE,
            SelectRacePacket.STREAM_CODEC,
            RaceHandler::handleSelectRacePacket
        )


        registrar.playToClient(
            WeatherSyncPacket.TYPE,
            WeatherSyncPacket.STREAM_CODEC,
            ClientWeatherEngine.Handler::handleWeatherSync
        )

        registrar.playToClient(
            BodyHeatSyncPacket.TYPE,
            BodyHeatSyncPacket.STREAM_CODEC,
            BodyHeatHandler::handleSync
        )

        registrar.playToClient(
            CalendarSyncPacket.TYPE,
            CalendarSyncPacket.STREAM_CODEC,
            Calendar.ClientHandler::handleTimeSync
        )
    }
}