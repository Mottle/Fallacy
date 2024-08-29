package dev.deepslate.fallacy.common.network.packet

import dev.deepslate.fallacy.Fallacy
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

data class RaceIdSyncPacket(val raceId: String) : CustomPacketPayload {

    companion object {
        val TYPE = CustomPacketPayload.Type<RaceIdSyncPacket>(Fallacy.id("race_id_sync_packet"))

        val STREAM_CODEC =
            StreamCodec.composite(ByteBufCodecs.STRING_UTF8, RaceIdSyncPacket::raceId, ::RaceIdSyncPacket)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> = TYPE
}