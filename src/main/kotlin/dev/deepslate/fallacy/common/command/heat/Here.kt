package dev.deepslate.fallacy.common.command.heat

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import dev.deepslate.fallacy.thermodynamics.data.Temperature
import dev.deepslate.fallacy.thermodynamics.impl.EnvironmentThermodynamicsEngine
import dev.deepslate.fallacy.util.command.GameCommand
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel

class Here : GameCommand {
    override val source: String = "fallacy heat here"

    override val suggestions: Map<String, SuggestionProvider<CommandSourceStack>> = emptyMap()

    override val permissionRequired: String? = "fallacy.heat.here"

    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        val player = context.source.entity ?: return 0
        val level = player.level() as? ServerLevel ?: return 0
        val engine = EnvironmentThermodynamicsEngine.getEnvironmentEngineOrNull(level) ?: return 0
        val heat = engine.getHeat(player.onPos)
        player.sendSystemMessage(Component.literal(Temperature.kelvins(heat).toCelsius().toString()))

        return Command.SINGLE_SUCCESS
    }
}