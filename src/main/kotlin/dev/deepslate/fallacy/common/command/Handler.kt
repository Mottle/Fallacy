package dev.deepslate.fallacy.common.command

import dev.deepslate.fallacy.Fallacy
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.RegisterCommandsEvent

@EventBusSubscriber(modid = Fallacy.MOD_ID)
object Handler {

    @SubscribeEvent
    fun onRegisterCommands(event: RegisterCommandsEvent) {
        val dispatcher = event.dispatcher

        FallacyCommands.commands.forEach { cmd ->
            val finalCommand = FallacyCommands.getPrefix().then(cmd.command)
            dispatcher.register(finalCommand)
        }
    }
}