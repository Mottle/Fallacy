package dev.deepslate.fallacy.common.network.packet

import dev.deepslate.fallacy.Fallacy
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

data class CalendarSyncPacket(val currentTime: Long) : CustomPacketPayload {
    companion object {
        val TYPE = CustomPacketPayload.Type<CalendarSyncPacket>(Fallacy.withID("calendar_sync"))

        val STREAM_CODEC: StreamCodec<ByteBuf, CalendarSyncPacket> =
            StreamCodec.composite(ByteBufCodecs.VAR_LONG, CalendarSyncPacket::currentTime, ::CalendarSyncPacket)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}