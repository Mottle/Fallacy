package dev.deepslate.fallacy.common.network.packet

import dev.deepslate.fallacy.Fallacy
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation

data class SelectRacePacket(val raceId: ResourceLocation) : CustomPacketPayload {
    companion object {
        val TYPE = CustomPacketPayload.Type<SelectRacePacket>(Fallacy.withID("select_race_packet"))

        val STREAM_CODEC: StreamCodec<ByteBuf, SelectRacePacket> =
            StreamCodec.composite(ResourceLocation.STREAM_CODEC, SelectRacePacket::raceId, ::SelectRacePacket)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}