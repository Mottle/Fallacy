package dev.deepslate.fallacy.common.command

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.util.command.CommandConverter
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.RegisterCommandsEvent

@EventBusSubscriber(modid = Fallacy.MOD_ID)
object Handler {

    @SubscribeEvent
    fun onRegisterCommands(event: RegisterCommandsEvent) {
        val dispatcher = event.dispatcher
        val converter = CommandConverter()

        FallacyCommands.commands.forEach { cmd ->
            val raw = converter.convert(cmd)
            dispatcher.register(raw)
        }
    }
}