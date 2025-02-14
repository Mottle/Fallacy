package dev.deepslate.fallacy.common.capability.heat

import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.common.network.packet.BodyHeatSyncPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.network.PacketDistributor
import kotlin.math.min

class PlayerBodyHeat(val player: Player) : IBodyHeat {
    override var heat: Float
        get() = player.getData(FallacyAttachments.BODY_HEAT)
        set(value) {
            player.setData(FallacyAttachments.BODY_HEAT, value)
        }

    // dt = k * rate * diff
    override fun tick(env: Float, resistance: Float) {
        if (heat == env) return

        val diff = env - heat
        val k = 0.0012f
        val rate = 1f - resistance
        val dt = k * rate * diff
        val finalHeat = heat + dt

        heat = if (heat < env) min(finalHeat, env) else finalHeat
    }

    fun sync() {
        if (player is ServerPlayer) {
            PacketDistributor.sendToPlayer(player, BodyHeatSyncPacket(heat))
        }
    }
}