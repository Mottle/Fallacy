package dev.deepslate.fallacy.common.network.packet

import dev.deepslate.fallacy.Fallacy
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

data class CladdingPacket(val index: Int) : CustomPacketPayload {

    companion object {
        val TYPE = CustomPacketPayload.Type<CladdingPacket>(Fallacy.id("cladding_packet"))

        val STREAM_CODEC =
            StreamCodec.composite(ByteBufCodecs.INT, CladdingPacket::index, ::CladdingPacket)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> = TYPE
}