package dev.deepslate.fallacy.rule.item

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.util.TickHelper
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.phys.Vec3
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.tick.ServerTickEvent
import kotlin.math.atan2

object ElytraSpeedRule {

    private const val MAX_HORIZONTAL_SPEED = 1.66f

    private const val SMOOTH_FACTOR = 0.3

    private const val MULTIPLIER = 0.6

    private const val THRESHOLD = MULTIPLIER * (1 - SMOOTH_FACTOR) * MAX_HORIZONTAL_SPEED

    fun reduceFlySpeed(player: ServerPlayer) {
        val speedVec = player.deltaMovement
        val speed = speedVec.length()

        if (speed > THRESHOLD && checkHorizontalMove(speedVec)) {
            val target = speedVec.normalize().scale(MULTIPLIER * speed)
            val diff = speedVec.subtract(target)
            val newSpeedVec = speedVec.subtract(diff.scale(SMOOTH_FACTOR))

            player.deltaMovement = newSpeedVec
            player.hurtMarked = true
            player.onUpdateAbilities()
        }
    }

    private val maxRad = Math.toRadians(50.0)

    private val minRad = Math.toRadians(-50.0)

    private fun checkHorizontalMove(vec: Vec3): Boolean {
        val verticalVelocity = vec.y
        val horizontalVelocity = vec.horizontalDistance()
        val angle = atan2(verticalVelocity, horizontalVelocity)
        return angle in minRad..maxRad
    }

    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object Handler {
        private const val TICK_RATE = 4

        @SubscribeEvent
        fun onServerTick(event: ServerTickEvent.Post) {
            if (!TickHelper.checkServerTickRate(TICK_RATE)) return

            val players = event.server.playerList.players
            players.filter { it.isFallFlying }.forEach(::reduceFlySpeed)
        }
    }
}