package dev.deepslate.fallacy.common.network.packet

import dev.deepslate.fallacy.Fallacy
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation

data class RaceIdSyncPacket(val raceId: ResourceLocation) : CustomPacketPayload {

    companion object {
        val TYPE = CustomPacketPayload.Type<RaceIdSyncPacket>(Fallacy.id("race_id_sync_packet"))

        val STREAM_CODEC =
            StreamCodec.composite(ResourceLocation.STREAM_CODEC, RaceIdSyncPacket::raceId, ::RaceIdSyncPacket)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}