package dev.deepslate.fallacy.util

import dev.deepslate.fallacy.Fallacy
import net.minecraft.server.MinecraftServer
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.server.ServerStartedEvent
import net.neoforged.neoforge.event.server.ServerStoppedEvent

data object ServerHelper {
    var server: MinecraftServer? = null
        private set

    @EventBusSubscriber(modid = Fallacy.MOD_ID) // 不要标Dist
    object Handler {
        @SubscribeEvent
        fun onServerStarted(event: ServerStartedEvent) {
            Fallacy.LOGGER.info("Server Helper catch server instance.")
            server = event.server
        }

        @SubscribeEvent
        fun onServerStopped(event: ServerStoppedEvent) {
            Fallacy.LOGGER.info("Server Helper instance removed.")
            server = null
        }
    }
}