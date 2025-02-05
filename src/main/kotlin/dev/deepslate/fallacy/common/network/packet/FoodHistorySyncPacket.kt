package dev.deepslate.fallacy.common.network.packet

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.player.FoodHistory
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

data class FoodHistorySyncPacket(val history: FoodHistory) : CustomPacketPayload {

    companion object {
        val TYPE = CustomPacketPayload.Type<FoodHistorySyncPacket>(Fallacy.id("food_history_sync_packet"))

        val STREAM_CODEC: StreamCodec<ByteBuf, FoodHistorySyncPacket> =
            StreamCodec.composite(FoodHistory.STREAM_CODEC, FoodHistorySyncPacket::history, ::FoodHistorySyncPacket)
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}