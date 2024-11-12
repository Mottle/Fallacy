package dev.deepslate.fallacy.weather.current

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.util.TickHelper
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.tick.LevelTickEvent

object CurrentSimulatorManager {
    private var simulator: CurrentSimulator? = null

    fun set(simulator: CurrentSimulator) {
        this.simulator = simulator
    }

    fun get(): CurrentSimulator? = simulator

    fun tick() = simulator?.tick()

    const val SEC = 20

    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object Handler {
        @SubscribeEvent
        fun onLevelTick(event: LevelTickEvent.Pre) {
            if (event.level.isClientSide) return
            if (!TickHelper.checkServerSecondRate(SEC)) return
            if (event.level.dimensionType() != Level.OVERWORLD) return
            if (get() == null) {
                val level = event.level as ServerLevel
                simulator = RandomCurrentSimulator(level)
            }

            tick()
        }
    }
}