package dev.deepslate.fallacy.common.network

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.capability.FallacyCapabilities
import dev.deepslate.fallacy.common.network.packet.ThirstSyncPacket
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent

@EventBusSubscriber(modid = Fallacy.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object FallacyNetwork {

    const val NETWORK_VERSION = "MONKEY"

    @SubscribeEvent
    fun register(event: RegisterPayloadHandlersEvent) {
        val registrar = event.registrar(NETWORK_VERSION)

        registrar.playToClient(ThirstSyncPacket.TYPE, ThirstSyncPacket.STREAM_CODEC) {
            data, ctx -> ctx.player().getCapability(FallacyCapabilities.THIRST)!!.thirst = data.data
        }
    }
}