package dev.deepslate.fallacy.common.data.player

import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.common.network.packet.FoodHistorySyncPacket
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext

data class FoodHistory(val foods: List<ResourceLocation> = listOf()) {

    companion object {
        const val MAX_SIZE = 10

        val CODEC = RecordCodecBuilder.create { instance ->
            instance.group(
                ResourceLocation.CODEC.sizeLimitedListOf(MAX_SIZE).fieldOf("foods").forGetter(FoodHistory::foods)
            ).apply(instance, ::FoodHistory)
        }

        val STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC.apply(ByteBufCodecs.list()), FoodHistory::foods,
            ::FoodHistory
        )
    }

    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object Handler {
        fun handleSync(data: FoodHistorySyncPacket, context: IPayloadContext) {
            context.player().setData(FallacyAttachments.FOOD_HISTORY, data.history)
            Fallacy.LOGGER.info("Syncing food history.")
        }

        @SubscribeEvent
        fun onPlayerJoin(event: PlayerEvent.PlayerLoggedInEvent) {
            PacketDistributor.sendToPlayer(
                event.entity as ServerPlayer,
                FoodHistorySyncPacket(event.entity.getData(FallacyAttachments.FOOD_HISTORY))
            )
        }

        @SubscribeEvent
        fun onPlayerRespawn(event: PlayerEvent.PlayerRespawnEvent) {
            PacketDistributor.sendToPlayer(
                event.entity as ServerPlayer,
                FoodHistorySyncPacket(event.entity.getData(FallacyAttachments.FOOD_HISTORY))
            )
        }
    }

    val size: Int
        get() = foods.size

    fun add(id: ResourceLocation): FoodHistory {
        if (size >= MAX_SIZE) return FoodHistory(foods.dropLast(1) + id)
        return FoodHistory(foods + id)
    }

    fun addFood(food: ItemStack): FoodHistory {
        if (!BuiltInRegistries.ITEM.containsValue(food.item)) return this

        val id = BuiltInRegistries.ITEM.getKey(food.item)
        return add(id)
    }

    fun count(id: ResourceLocation): Int = foods.count { it == id }

    fun countFood(food: ItemStack): Int = count(BuiltInRegistries.ITEM.getKey(food.item))
}