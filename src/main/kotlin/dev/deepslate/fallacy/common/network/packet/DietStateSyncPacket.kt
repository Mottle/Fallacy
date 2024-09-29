package dev.deepslate.fallacy.common.network.packet

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.player.DietState
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class DietStateSyncPacket(val state: DietState) : CustomPacketPayload {

    companion object {
        val TYPE = CustomPacketPayload.Type<DietStateSyncPacket>(Fallacy.id("diet_state_sync_packet"))

        val STREAM_CODEC =
            StreamCodec.composite(DietState.STREAM_CODEC, DietStateSyncPacket::state, ::DietStateSyncPacket)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> = TYPE
}