package dev.deepslate.fallacy.trait

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.util.TickHelper
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.tick.PlayerTickEvent

@EventBusSubscriber(modid = Fallacy.MOD_ID)
object Handler {

    @SubscribeEvent
    fun onServerPlayerTick(event: PlayerTickEvent.Post) {
        if (event.entity.level().isClientSide) return

        val player = event.entity as ServerPlayer
        val level = player.level() as ServerLevel
        val container = Trait.get(player)

        for (behavior in container) {
            if (behavior !is TickTrait) continue
            if (!TickHelper.checkServerTickRate(behavior.interval)) continue

            behavior.tick(level, player, player.blockPosition())
        }
    }
}