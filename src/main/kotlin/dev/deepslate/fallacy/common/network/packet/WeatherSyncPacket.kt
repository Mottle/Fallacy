package dev.deepslate.fallacy.common.network.packet

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.weather.WeatherInstance
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload

class WeatherSyncPacket(val weathers: List<WeatherInstance>): CustomPacketPayload {

    companion object {
        val TYPE = CustomPacketPayload.Type<WeatherSyncPacket>(Fallacy.id("weather_sync_packet"))

        val STREAM_CODEC = StreamCodec.composite(
            WeatherInstance.STREAM_CODEC.apply(ByteBufCodecs.list()), WeatherSyncPacket::weathers, ::WeatherSyncPacket
        )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}