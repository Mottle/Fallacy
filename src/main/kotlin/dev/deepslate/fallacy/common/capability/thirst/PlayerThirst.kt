package dev.deepslate.fallacy.common.capability.thirst

import dev.deepslate.fallacy.common.capability.FallacyDamageTypes
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.common.network.packet.ThirstSyncPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.network.PacketDistributor
import java.lang.Math.clamp
import kotlin.math.min

class PlayerThirst(val player: Player) : IThirst {

    companion object {
        private const val UPDATE_INTERVAL_TICKS = 20 * 60 * 2
    }

    override var value: Float
        get() = player.getData(FallacyAttachments.THIRST)
        set(value) {
            player.setData(FallacyAttachments.THIRST, value)
            if (player is ServerPlayer) {
                PacketDistributor.sendToPlayer(player, ThirstSyncPacket(this@PlayerThirst.value))
            }
        }
    override val max: Float = 20f

    override fun drink(value: Float) {
        this@PlayerThirst.value = min(max, this@PlayerThirst.value + value)
    }

    private fun loss() = 1f

    override fun tick() {
        if (player.isInvulnerable) return

//        val ticks = player.getData(FallacyAttachments.THIRST_TICKS)
        if (player.tickCount % UPDATE_INTERVAL_TICKS == 0) {
            value = clamp(value - loss(), 0f, max)
        }

        if (player.tickCount % 20 == 0 && value <= 0f) {
            val damage = damage(player)
            player.hurt(damage, 1f)
        }

//        player.setData(FallacyAttachments.THIRST_TICKS, ticks + 1)
    }

    private fun damage(player: Player): DamageSource =
        player.level().damageSources().source(FallacyDamageTypes.DEHYDRATION)
}