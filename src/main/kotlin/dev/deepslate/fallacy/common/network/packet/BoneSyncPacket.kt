package dev.deepslate.fallacy.common.network.packet

import dev.deepslate.fallacy.Fallacy
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

data class BoneSyncPacket(val bone: Float) : CustomPacketPayload {

    companion object {
        val TYPE = CustomPacketPayload.Type<BoneSyncPacket>(Fallacy.id("bone"))

        val STREAM_CODEC: StreamCodec<ByteBuf, BoneSyncPacket> =
            StreamCodec.composite(ByteBufCodecs.FLOAT, BoneSyncPacket::bone, ::BoneSyncPacket)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}