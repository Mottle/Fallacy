package dev.deepslate.fallacy.common.network.packet

import dev.deepslate.fallacy.Fallacy
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class DrinkInWorldPacket : CustomPacketPayload {

    companion object {
        val TYPE = CustomPacketPayload.Type<DrinkInWorldPacket>(Fallacy.withID("drink_in_world_packet"))

        val PACKET = DrinkInWorldPacket()

        val STREAM_CODEC: StreamCodec<ByteBuf, DrinkInWorldPacket> =
            StreamCodec.unit<ByteBuf, DrinkInWorldPacket>(PACKET)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}