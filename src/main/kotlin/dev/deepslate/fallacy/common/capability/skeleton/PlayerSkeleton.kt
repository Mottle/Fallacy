package dev.deepslate.fallacy.common.capability.skeleton

import dev.deepslate.fallacy.common.capability.Synchronous
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.common.data.FallacyAttributes
import dev.deepslate.fallacy.common.network.packet.BoneSyncPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.network.PacketDistributor

class PlayerSkeleton(val player: Player) : ISkeleton, Synchronous {

    override var bone: Float
        get() = player.getData(FallacyAttachments.BONE)
        set(value) {
            player.setData(FallacyAttachments.BONE, value.coerceIn(0f, max))
        }

    override val max: Float
        get() = player.getAttributeValue(FallacyAttributes.MAX_BONE).toFloat()

    override fun synchronize() {
        if (player is ServerPlayer) {
            PacketDistributor.sendToPlayer(player, BoneSyncPacket(bone))
        }
    }
}