package dev.deepslate.fallacy.permission

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.permission.integration.LuckPermsProvider
import dev.deepslate.fallacy.permission.integration.SimplePermissionProvider
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.fml.loading.FMLLoader
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent
import org.apache.logging.log4j.LogManager

private var instance: PermissionProvider = SimplePermissionProvider()

object PermissionManager : PermissionProvider by instance {
    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object Handler {
        private val logger = LogManager.getLogger("fallacy-permission")

        @SubscribeEvent
        fun onServerAboutToStart(event: ServerAboutToStartEvent) {
            val luckPerms = FMLLoader.getLoadingModList().mods.any { mod -> mod.modId == "luckperms" }

            if (luckPerms) {
                instance = LuckPermsProvider()
            }
            logger.info("Using ${instance.name} for permissions")
        }
    }
}