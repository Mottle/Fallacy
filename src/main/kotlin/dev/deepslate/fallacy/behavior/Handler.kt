package dev.deepslate.fallacy.behavior

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.behavior.impl.BurningInSunlight
import dev.deepslate.fallacy.behavior.impl.Weakness2InSunlight
import dev.deepslate.fallacy.behavior.impl.WeaknessInDay
import dev.deepslate.fallacy.behavior.impl.WeaknessInSunlight
import dev.deepslate.fallacy.util.TickHelper
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.tick.PlayerTickEvent

@EventBusSubscriber(modid = Fallacy.MOD_ID)
object Handler {
    private val behaviorMap = mutableMapOf<Int, MutableList<TickableBehavior>>()

    private fun addBehavior(behavior: TickableBehavior) {
        if (!behaviorMap.contains(behavior.interval)) behaviorMap[behavior.interval] = mutableListOf()
        behaviorMap[behavior.interval]!!.add(behavior)
    }

    @SubscribeEvent
    fun onServerPlayerTick(event: PlayerTickEvent.Post) {
        if (event.entity.level().isClientSide) return

        val player = event.entity as ServerPlayer
        val level = player.level() as ServerLevel

        for ((interval, behaviors) in behaviorMap) {
            if (!TickHelper.checkServerTickRate(interval)) continue
            behaviors.filter { it.check(player) }.forEach { it.tick(level, player, player.blockPosition()) }
        }
    }

    init {
//        Loader("Behavior").load<TickableBehavior>(::addBehavior)
        addBehavior(BurningInSunlight())
        addBehavior(WeaknessInDay())
        addBehavior(Weakness2InSunlight())
        addBehavior(WeaknessInSunlight())
    }
}