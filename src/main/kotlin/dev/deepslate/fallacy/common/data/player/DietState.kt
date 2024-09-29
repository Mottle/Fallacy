package dev.deepslate.fallacy.common.data.player

import com.mojang.datafixers.util.Either
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.common.item.component.DietData
import dev.deepslate.fallacy.common.network.packet.DietStateSyncPacket
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

data class DietState(
    val carbohydrate: Diet = Diet.State(90f),
    val protein: Diet = Diet.State(90f),
    val fat: Diet = Diet.State(90f),
    val fiber: Diet = Diet.State(90f),
    val electrolyte: Diet = Diet.State(90f),
) {

    companion object {
        val CODEC = RecordCodecBuilder.create { instance ->
            instance.group(
                Diet.CODEC.fieldOf("carbohydrate").forGetter(DietState::carbohydrate),
                Diet.CODEC.fieldOf("protein").forGetter(DietState::protein),
                Diet.CODEC.fieldOf("fat").forGetter(DietState::fat),
                Diet.CODEC.fieldOf("fiber").forGetter(DietState::fiber),
                Diet.CODEC.fieldOf("electrolyte").forGetter(DietState::electrolyte)
            ).apply(instance, ::DietState)
        }

        val STREAM_CODEC = StreamCodec.composite(
            Diet.STREAM_CODEC, DietState::carbohydrate,
            Diet.STREAM_CODEC, DietState::protein,
            Diet.STREAM_CODEC, DietState::fat,
            Diet.STREAM_CODEC, DietState::fiber,
            Diet.STREAM_CODEC, DietState::electrolyte,
            ::DietState
        )

        fun noNeed() = DietState(Diet.NoNeed, Diet.NoNeed, Diet.NoNeed, Diet.NoNeed, Diet.NoNeed)
    }

    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object Handler {
        internal fun handleSync(data: DietStateSyncPacket, context: IPayloadContext) {
            val player = context.player()
            player.setData(FallacyAttachments.DIET_STATE, data.state)
        }

        @SubscribeEvent
        fun onPlayerJoin(event: PlayerEvent.PlayerLoggedInEvent) {
            val player = event.entity as? ServerPlayer ?: return
            val data = player.getData(FallacyAttachments.DIET_STATE)
            val packet = DietStateSyncPacket(data)
            PacketDistributor.sendToPlayer(player, packet)
        }
    }

    sealed class Diet {
        abstract fun add(value: Float): Diet

        object NoNeed : Diet() {
            override fun add(value: Float): Diet = NoNeed
        }

        class State(val value: Float) : Diet() {
            override fun add(value: Float): Diet = State((this.value + value).coerceIn(DIET_RANGE_MIN, DIET_RANGE_MAX))
        }

        companion object {
            const val DIET_RANGE_MAX = 100f

            const val DIET_RANGE_MIN = 0f

            private val NO_NEED_CODEC: Codec<NoNeed> = RecordCodecBuilder.create { instance ->
                instance.group(Codec.STRING.fieldOf("value").forGetter { "no_need" }).apply(instance) { _ -> NoNeed }
            }

            private val NO_NEED_STREAM_CODEC: StreamCodec<ByteBuf, NoNeed> = StreamCodec.unit(NoNeed)

            private val STATE_CODEC: Codec<State> = RecordCodecBuilder.create { instance ->
                instance.group(Codec.FLOAT.fieldOf("value").forGetter(State::value)).apply(instance, ::State)
            }

            private val STATE_STREAM_CODEC: StreamCodec<ByteBuf, State> =
                StreamCodec.composite(ByteBufCodecs.FLOAT, State::value, ::State)

            private fun toEither(diet: Diet): Either<NoNeed, State> = when (diet) {
                is NoNeed -> Either<NoNeed, State>.left(NoNeed)
                is State -> Either<NoNeed, State>.right(diet)
            }

            private fun fromEither(either: Either<NoNeed, State>): Diet =
                if (either.right().isPresent) either.right().get() else NoNeed

            val CODEC = Codec.either(NO_NEED_CODEC, STATE_CODEC).xmap(::fromEither, ::toEither)

            val STREAM_CODEC =
                ByteBufCodecs.either(NO_NEED_STREAM_CODEC, STATE_STREAM_CODEC).map(::fromEither, ::toEither)
        }
    }

    fun add(data: DietData) = copy(
        carbohydrate.add(data.carbohydrate),
        protein.add(data.protein),
        fat.add(data.fat),
        fiber.add(data.fiber),
        electrolyte.add(data.electrolyte)
    )

    fun set(player: Player) {
        player.setData(FallacyAttachments.DIET_STATE, this)
    }
}