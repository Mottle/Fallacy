package dev.deepslate.fallacy.hydration.thirst

import dev.deepslate.fallacy.common.capability.FallacyAttachments
import dev.deepslate.fallacy.common.network.packet.ThirstSyncPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.network.PacketDistributor
import java.lang.Math.clamp
import kotlin.math.min

class PlayerThirst(val player: Player): IThirst {

    companion object {
        private const val UPDATE_RATE = 20 * 30
    }

    override var thirst: Float
        get() = player.getData(FallacyAttachments.THIRST)
        set(value) {
            player.setData(FallacyAttachments.THIRST, value)
            if(player is ServerPlayer) {
                PacketDistributor.sendToPlayer(player, ThirstSyncPacket(thirst))
            }
        }
    override val max: Float = 20f

    override fun drink(value: Float) {
        thirst = min(max, thirst + value)
    }

    private fun loss() = 1f

    override fun tick() {
        if(player.isInvulnerable) return

//        val ticks = player.getData(FallacyAttachments.THIRST_TICKS)
        if(player.tickCount % UPDATE_RATE == 0) {
            thirst = clamp(thirst - loss(), 0f, max)
        }

        if(thirst <= 0f) player.kill()

//        player.setData(FallacyAttachments.THIRST_TICKS, ticks + 1)
    }
}