package dev.deepslate.fallacy.weather.wind

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.util.extension.internalWindEngine
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.level.LevelEvent

class ClientWindEngine : WindEngine {
    override fun tick() {
    }

    override fun getWindAt(pos: BlockPos): Vec3 {
        TODO("Not yet implemented")
    }

    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object Handler {
        @SubscribeEvent
        fun onClientLevelLoad(event: LevelEvent.Load) {
            if (!event.level.isClientSide) return
            if ((event.level as ClientLevel).dimension() != Level.OVERWORLD) return
            val level = event.level as ClientLevel

            level.internalWindEngine = ClientWindEngine()
        }
    }
}