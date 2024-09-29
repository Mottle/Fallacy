package dev.deepslate.fallacy.common.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class DietData(
    val carbohydrate: Float = 0f,
    val protein: Float = 0f,
    val fat: Float = 0f,
    val fiber: Float = 0f,
    val electrolyte: Float = 0f
) {
    companion object {

        const val ITEM_DIET_MAX = 100f

        const val ITEM_DIET_MIN = -100f

        val CODEC = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.floatRange(ITEM_DIET_MIN, ITEM_DIET_MAX).fieldOf("carbohydrate").forGetter(DietData::electrolyte),
                Codec.floatRange(ITEM_DIET_MIN, ITEM_DIET_MAX).fieldOf("protein").forGetter(DietData::protein),
                Codec.floatRange(ITEM_DIET_MIN, ITEM_DIET_MAX).fieldOf("fat").forGetter(DietData::fat),
                Codec.floatRange(ITEM_DIET_MIN, ITEM_DIET_MAX).fieldOf("fiber").forGetter(DietData::fiber),
                Codec.floatRange(ITEM_DIET_MIN, ITEM_DIET_MAX).fieldOf("electrolyte").forGetter(DietData::electrolyte)
            ).apply(instance, ::DietData)
        }

        val STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, DietData::electrolyte,
            ByteBufCodecs.FLOAT, DietData::protein,
            ByteBufCodecs.FLOAT, DietData::fat,
            ByteBufCodecs.FLOAT, DietData::fiber,
            ByteBufCodecs.FLOAT, DietData::electrolyte,
            ::DietData
        )
    }

    fun toComponents() = listOf(
        Component.translatable("item.fallacy.diet_data.carbohydrate").append(": $carbohydrate"),
        Component.translatable("item.fallacy.diet_data.protein").append(": $protein"),
        Component.translatable("item.fallacy.diet_data.fat").append(": $fat"),
        Component.translatable("item.fallacy.diet_data.fiber").append(": $fiber"),
        Component.translatable("item.fallacy.diet_data.electrolyte").append(": $electrolyte")
    )

//    fun toComponent() = toComponents().reduce { c, then -> c.append(then) }
}