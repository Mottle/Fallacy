package dev.deepslate.fallacy.race

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.common.network.packet.RaceIdSyncPacket
import dev.deepslate.fallacy.race.impl.Unknown
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.network.PacketDistributor

abstract class Race {
    abstract val namespacedId: ResourceLocation

//    abstract val attribute: PlayerAttribute
//
//    abstract val nutrition: NutritionState

    abstract val resources: Map<String, Resource>

    open fun tick(level: ServerLevel, player: ServerPlayer, position: BlockPos) {}

    open fun apply(player: ServerPlayer) {}

    open fun deapply(player: ServerPlayer) {}

    fun `is`(race: Race) = this.namespacedId == race.namespacedId

    fun `is`(holder: Holder<Race>) = `is`(holder.value())

//    init {
//        require(resources.contains(AttributeResource.KEY)) { "Race $namespacedId must have attribute resource" }
//        require(resources.contains(NutritionResource.KEY)) { "Race $namespacedId must have nutrition state resource" }
//    }

    companion object {
        const val RACE_TICK_RATE = 2 * 20

        fun sync(player: ServerPlayer) {
            val raceId = player.getData(FallacyAttachments.RACE_ID)
            val packet = RaceIdSyncPacket(raceId)

            PacketDistributor.sendToPlayer(player, packet)
            Fallacy.LOGGER.info("Syncing race id: $raceId to player [${player.name.string}, ${player.uuid}].")
        }

        fun getRaceId(player: Player): ResourceLocation = player.getData(FallacyAttachments.RACE_ID)

        fun get(player: Player): Race {
            val raceId = getRaceId(player)
            val race = Races.REGISTRY.get(raceId) ?: Unknown.INSTANCE
            return race
        }

        fun contains(raceId: ResourceLocation) = Races.REGISTRY.containsKey(raceId)

        fun get(raceId: ResourceLocation) = Races.REGISTRY.get(raceId)

        fun getOrUnknown(raceId: ResourceLocation) = get(raceId) ?: Unknown.INSTANCE

        fun setNewRace(player: ServerPlayer, race: Race) {
            val oldRace = get(player)
//            val diet = player.getCapability(FallacyCapabilities.DIET)!!

            oldRace.deapply(player)
            player.setData(FallacyAttachments.RACE_ID, race.namespacedId)
//            race.attribute.set(player)
//            diet.nutrition = race.nutrition

//            if (diet is Synchronous) {
//                diet.synchronize()
//            }
            race.resources.values.forEach {
                it.apply(player)
            }

            race.apply(player)
            sync(player)
        }
    }

    interface Resource {
        fun apply(player: Player)

        fun deapply(player: Player)
    }
}