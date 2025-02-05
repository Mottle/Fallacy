package dev.deepslate.fallacy.weather.current

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.util.TickHelper
import dev.deepslate.fallacy.util.extension.currentSimulator
import dev.deepslate.fallacy.util.extension.internalCurrentSimulator
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.level.LevelEvent
import net.neoforged.neoforge.event.tick.LevelTickEvent

object CurrentSimulatorManager {

    const val SEC = 20

    private var defaultCurrentSimulatorGetter: (ServerLevel) -> CurrentSimulator = ::RandomCurrentSimulator

    fun useDefault(getter: (ServerLevel) -> CurrentSimulator) {
        defaultCurrentSimulatorGetter = getter
    }

    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object Handler {
        @SubscribeEvent
        fun onServerLevelLoad(event: LevelEvent.Load) {
            if (event.level.isClientSide) return
            val level = event.level as ServerLevel
            level.internalCurrentSimulator = defaultCurrentSimulatorGetter(level)
        }

        @SubscribeEvent
        fun onLevelTick(event: LevelTickEvent.Pre) {
            if (event.level.isClientSide) return
            if (!TickHelper.checkServerSecondRate(SEC)) return
            if (event.level.dimensionType() != Level.OVERWORLD) return
            val level = event.level as ServerLevel

            level.currentSimulator?.tick()
        }
    }
}