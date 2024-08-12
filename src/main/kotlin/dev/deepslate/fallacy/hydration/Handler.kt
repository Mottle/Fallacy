package dev.deepslate.fallacy.hydration

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.capability.FallacyAttachments.THIRST
import dev.deepslate.fallacy.common.capability.FallacyCapabilities
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent
import net.neoforged.neoforge.event.tick.PlayerTickEvent

@EventBusSubscriber(modid = Fallacy.MOD_ID)
object Handler {
    @SubscribeEvent
    fun onPlayerTick(event: PlayerTickEvent.Post) {
        val player = event.entity as? ServerPlayer ?: return
        val thirst = player.getCapability(FallacyCapabilities.THIRST)!!

        thirst.tick()
    }

    @SubscribeEvent
    fun onPlayerRightClick(event: PlayerInteractEvent.RightClickEmpty) {
        val player = event.entity
        val thirst = player.getCapability(FallacyCapabilities.THIRST)!!

        if(thirst.thirst >= thirst.max) return
        if(event.side.isClient) {
            player.swing(InteractionHand.MAIN_HAND)
            return
        }

        val level = player.level()
        val pos = player.blockPosition()
        level.playSound(null, pos, SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS)
    }

    @SubscribeEvent
    fun endFix(event: PlayerEvent.Clone) {
        val origin = event.original
        if(!event.isWasDeath) {
            val player = event.entity
            val oldThirst = origin.getData(THIRST)
//            val oldThirstTicks = origin.getData(THIRST_TICKS)

            player.setData(THIRST, oldThirst)
//            player.setData(THIRST_TICKS, oldThirstTicks)
        }
    }
}