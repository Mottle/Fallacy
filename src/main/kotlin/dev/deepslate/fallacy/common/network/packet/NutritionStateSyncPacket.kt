package dev.deepslate.fallacy.common.network.packet

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.player.NutritionState
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class NutritionStateSyncPacket(val state: NutritionState) : CustomPacketPayload {

    companion object {
        val TYPE = CustomPacketPayload.Type<NutritionStateSyncPacket>(Fallacy.withID("nutrition_state_sync_packet"))

        val STREAM_CODEC: StreamCodec<ByteBuf, NutritionStateSyncPacket> =
            StreamCodec.composite(
                NutritionState.STREAM_CODEC,
                NutritionStateSyncPacket::state,
                ::NutritionStateSyncPacket
            )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}