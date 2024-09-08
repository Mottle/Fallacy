package dev.deepslate.fallacy.race

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import dev.deepslate.fallacy.common.network.packet.RaceIdSyncPacket
import dev.deepslate.fallacy.race.impl.Unknown
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.neoforged.neoforge.network.PacketDistributor

interface Race {
    val namespacedId: ResourceLocation

    val attribute: PlayerAttribute

    fun tick(level: ServerLevel, player: ServerPlayer, position: BlockPos)

    fun set(player: ServerPlayer)

    companion object {
        fun sync(player: ServerPlayer) {
            val raceId = player.getData(FallacyAttachments.RACE_ID)
            val packet = RaceIdSyncPacket(raceId)

            PacketDistributor.sendToPlayer(player, packet)
            Fallacy.LOGGER.info("Syncing race id: $raceId to player [${player.name}, ${player.uuid}].")
        }

        fun getRaceId(player: ServerPlayer) = player.getData(FallacyAttachments.RACE_ID)

        fun get(player: ServerPlayer): Race {
            val raceId = getRaceId(player)
            val race = FallacyRaces.REGISTRY.get(raceId) ?: Unknown.INSTANCE
            return race
        }

        fun contains(raceId: ResourceLocation) = FallacyRaces.REGISTRY.containsKey(raceId)

        fun get(raceId: ResourceLocation) = FallacyRaces.REGISTRY.get(raceId)

        fun getOrUnknown(raceId: ResourceLocation) = get(raceId) ?: Unknown.INSTANCE
    }
}