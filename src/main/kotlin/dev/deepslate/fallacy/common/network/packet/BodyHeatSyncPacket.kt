package dev.deepslate.fallacy.common.network.packet

import dev.deepslate.fallacy.Fallacy
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

data class BodyHeatSyncPacket(val value: Float) : CustomPacketPayload {
    companion object {
        val TYPE = CustomPacketPayload.Type<BodyHeatSyncPacket>(Fallacy.id("body_heat_sync"))

        val STREAM_CODEC: StreamCodec<ByteBuf, BodyHeatSyncPacket> =
            StreamCodec.composite(ByteBufCodecs.FLOAT, BodyHeatSyncPacket::value, ::BodyHeatSyncPacket)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}