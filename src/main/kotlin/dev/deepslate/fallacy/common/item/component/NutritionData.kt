package dev.deepslate.fallacy.common.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec

data class NutritionData(
    val carbohydrate: Float = 0f,
    val protein: Float = 0f,
    val fat: Float = 0f,
    val fiber: Float = 0f,
    val electrolyte: Float = 0f
) {
    companion object {

        const val ITEM_DIET_MAX = 100f

        const val ITEM_DIET_MIN = -100f

        val CODEC: Codec<NutritionData> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.floatRange(ITEM_DIET_MIN, ITEM_DIET_MAX).fieldOf("carbohydrate")
                    .forGetter(NutritionData::electrolyte),
                Codec.floatRange(ITEM_DIET_MIN, ITEM_DIET_MAX).fieldOf("protein").forGetter(NutritionData::protein),
                Codec.floatRange(ITEM_DIET_MIN, ITEM_DIET_MAX).fieldOf("fat").forGetter(NutritionData::fat),
                Codec.floatRange(ITEM_DIET_MIN, ITEM_DIET_MAX).fieldOf("fiber").forGetter(NutritionData::fiber),
                Codec.floatRange(ITEM_DIET_MIN, ITEM_DIET_MAX).fieldOf("electrolyte")
                    .forGetter(NutritionData::electrolyte)
            ).apply(instance, ::NutritionData)
        }

        val STREAM_CODEC: StreamCodec<ByteBuf, NutritionData> = StreamCodec.composite(
            ByteBufCodecs.FLOAT, NutritionData::electrolyte,
            ByteBufCodecs.FLOAT, NutritionData::protein,
            ByteBufCodecs.FLOAT, NutritionData::fat,
            ByteBufCodecs.FLOAT, NutritionData::fiber,
            ByteBufCodecs.FLOAT, NutritionData::electrolyte,
            ::NutritionData
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