package dev.deepslate.fallacy.common.capability.heat

import dev.deepslate.fallacy.common.capability.Synchronous
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.common.data.FallacyAttributes
import dev.deepslate.fallacy.common.network.packet.BodyHeatSyncPacket
import dev.deepslate.fallacy.thermodynamics.ThermodynamicsEngine
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.network.PacketDistributor
import kotlin.math.min

class PlayerBodyHeat(val player: Player) : IBodyHeat, Synchronous {
    override var heat: Float
        get() = player.getData(FallacyAttachments.BODY_HEAT)
        set(value) {
            player.setData(FallacyAttachments.BODY_HEAT, value)
        }

    // dt = k * rate * diff
    override fun tick() {
        val serverPlayer = player as? ServerPlayer ?: return

        if (serverPlayer.isInvulnerable) return

        val envHeat = ThermodynamicsEngine.getHeatAt(serverPlayer).toFloat()
        val resistance = serverPlayer.getAttribute(FallacyAttributes.HEAT_RESISTANCE)!!.value.toFloat()

        if (heat == envHeat) return

        val diff = envHeat - heat
        val k = 0.0012f
        val rate = 1f - resistance
        val dt = k * rate * diff
        val finalHeat = heat + dt
        val newHeat = if (heat < envHeat) min(finalHeat, envHeat) else finalHeat

        if (heat != newHeat) {
            heat = newHeat
            synchronize()
        }
    }

    override fun synchronize() {
        if (player is ServerPlayer) {
            PacketDistributor.sendToPlayer(player, BodyHeatSyncPacket(heat))
        }
    }
}