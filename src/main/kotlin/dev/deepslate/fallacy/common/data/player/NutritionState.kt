package dev.deepslate.fallacy.common.data.player

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.common.item.component.NutritionData
import dev.deepslate.fallacy.common.network.packet.NutritionStateSyncPacket
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext

data class NutritionState(
    val carbohydrate: Nutrition = Nutrition.State(90f),
    val protein: Nutrition = Nutrition.State(90f),
    val fat: Nutrition = Nutrition.State(90f),
    val fiber: Nutrition = Nutrition.State(90f),
    val electrolyte: Nutrition = Nutrition.State(90f),
) {

    companion object {
        val CODEC: Codec<NutritionState> = RecordCodecBuilder.create { instance ->
            instance.group(
                Nutrition.CODEC.fieldOf("carbohydrate").forGetter(NutritionState::carbohydrate),
                Nutrition.CODEC.fieldOf("protein").forGetter(NutritionState::protein),
                Nutrition.CODEC.fieldOf("fat").forGetter(NutritionState::fat),
                Nutrition.CODEC.fieldOf("fiber").forGetter(NutritionState::fiber),
                Nutrition.CODEC.fieldOf("electrolyte").forGetter(NutritionState::electrolyte)
            ).apply(instance, ::NutritionState)
        }

        val STREAM_CODEC: StreamCodec<ByteBuf, NutritionState> = StreamCodec.composite(
            Nutrition.STREAM_CODEC, NutritionState::carbohydrate,
            Nutrition.STREAM_CODEC, NutritionState::protein,
            Nutrition.STREAM_CODEC, NutritionState::fat,
            Nutrition.STREAM_CODEC, NutritionState::fiber,
            Nutrition.STREAM_CODEC, NutritionState::electrolyte,
            ::NutritionState
        )

        fun noNeed() =
            NutritionState(Nutrition.NoNeed, Nutrition.NoNeed, Nutrition.NoNeed, Nutrition.NoNeed, Nutrition.NoNeed)
    }

    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object Handler {
        internal fun handleSync(data: NutritionStateSyncPacket, context: IPayloadContext) {
            val player = context.player()
            player.setData(FallacyAttachments.NUTRITION_STATE, data.state)
            Fallacy.LOGGER.info("Syncing nutrition data.")
        }

        @SubscribeEvent
        fun onPlayerJoin(event: PlayerEvent.PlayerLoggedInEvent) {
            val player = event.entity as? ServerPlayer ?: return
            val data = player.getData(FallacyAttachments.NUTRITION_STATE)
            val packet = NutritionStateSyncPacket(data)
            PacketDistributor.sendToPlayer(player, packet)
        }
    }

    sealed class Nutrition {
        abstract fun add(value: Float): Nutrition

        object NoNeed : Nutrition() {
            override fun add(value: Float): Nutrition = NoNeed
        }

        class State(val value: Float) : Nutrition() {
            override fun add(value: Float): Nutrition =
                State((this.value + value).coerceIn(NUTRITION_RANGE_MIN, NUTRITION_RANGE_MAX))
        }

        companion object {
            const val NUTRITION_RANGE_MAX = 100f

            const val NUTRITION_RANGE_MIN = 0f

            private val NO_NEED_CODEC: Codec<NoNeed> = RecordCodecBuilder.create { instance ->
                instance.group(Codec.STRING.fieldOf("value").forGetter { "no_need" }).apply(instance) { _ -> NoNeed }
            }

            private val NO_NEED_STREAM_CODEC: StreamCodec<ByteBuf, NoNeed> = StreamCodec.unit(NoNeed)

            private val STATE_CODEC: Codec<State> = RecordCodecBuilder.create { instance ->
                instance.group(
                    Codec.floatRange(NUTRITION_RANGE_MIN, NUTRITION_RANGE_MAX).fieldOf("value").forGetter(State::value)
                ).apply(instance, ::State)
            }

            private val STATE_STREAM_CODEC: StreamCodec<ByteBuf, State> =
                StreamCodec.composite(ByteBufCodecs.FLOAT, State::value, ::State)

            private fun toEither(nutrition: Nutrition): Either<NoNeed, State> = when (nutrition) {
                is NoNeed -> Either<NoNeed, State>.left(NoNeed)
                is State -> Either<NoNeed, State>.right(nutrition)
            }

            private fun fromEither(either: Either<NoNeed, State>): Nutrition =
                if (either.right().isPresent) either.right().get() else NoNeed

            val CODEC: Codec<Nutrition> = Codec.either(NO_NEED_CODEC, STATE_CODEC).xmap(::fromEither, ::toEither)

            val STREAM_CODEC: StreamCodec<ByteBuf, Nutrition> =
                ByteBufCodecs.either(NO_NEED_STREAM_CODEC, STATE_STREAM_CODEC).map(::fromEither, ::toEither)
        }
    }

    fun add(data: NutritionData) = copy(
        carbohydrate.add(data.carbohydrate),
        protein.add(data.protein),
        fat.add(data.fat),
        fiber.add(data.fiber),
        electrolyte.add(data.electrolyte)
    )

    fun set(player: Player) {
        player.setData(FallacyAttachments.NUTRITION_STATE, this)
    }
}