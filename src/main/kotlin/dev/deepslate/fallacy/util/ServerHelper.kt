package dev.deepslate.fallacy.util

import dev.deepslate.fallacy.Fallacy
import net.minecraft.server.MinecraftServer
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.server.ServerStartedEvent

@EventBusSubscriber(modid = Fallacy.MOD_ID, value = [Dist.DEDICATED_SERVER])
object ServerHelper {
    private var server: MinecraftServer? = null

    @SubscribeEvent
    fun onServerStarted(event: ServerStartedEvent) {
        server = event.server
    }

    val dedicatedServer: MinecraftServer?
        get() = server
}