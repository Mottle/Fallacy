package dev.deepslate.fallacy.common.item.data

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class DietData(
    val carbohydrate: Float = 0f,
    val protein: Float = 0f,
    val grease: Float = 0f,
    val fiber: Float = 0f,
    val electrolyte: Float = 0f
) {
    companion object {
        val CODEC = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.FLOAT.fieldOf("carbohydrate").forGetter(DietData::electrolyte),
                Codec.FLOAT.fieldOf("protein").forGetter(DietData::protein),
                Codec.FLOAT.fieldOf("grease").forGetter(DietData::grease),
                Codec.FLOAT.fieldOf("fiber").forGetter(DietData::fiber),
                Codec.FLOAT.fieldOf("electrolyte").forGetter(DietData::electrolyte)
            ).apply(instance, ::DietData)
        }

        val STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, DietData::electrolyte,
            ByteBufCodecs.FLOAT, DietData::protein,
            ByteBufCodecs.FLOAT, DietData::grease,
            ByteBufCodecs.FLOAT, DietData::fiber,
            ByteBufCodecs.FLOAT, DietData::electrolyte,
            ::DietData
        )
    }

    fun toComponents() = listOf(
        Component.translatable("item.fallacy.diet_data.carbohydrate").append(": $carbohydrate"),
        Component.translatable("item.fallacy.diet_data.protein").append(": $protein"),
        Component.translatable("item.fallacy.diet_data.grease").append(": $grease"),
        Component.translatable("item.fallacy.diet_data.fiber").append(": $fiber"),
        Component.translatable("item.fallacy.diet_data.electrolyte").append(": $electrolyte")
    )

//    fun toComponent() = toComponents().reduce { c, then -> c.append(then) }
}