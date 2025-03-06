package dev.deepslate.fallacy.common.command.heat

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import dev.deepslate.fallacy.thermodynamics.impl.EnvironmentThermodynamicsEngine
import dev.deepslate.fallacy.util.command.GameCommand
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component

class EngineState : GameCommand {
    override val source: String = "fallacy heat engine_state"

    override val suggestions: Map<String, SuggestionProvider<CommandSourceStack>> = emptyMap()

    override val permissionRequired: String? = "fallacy.command.heat.engine_state"

    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        val level = context.source.level
        val engine = EnvironmentThermodynamicsEngine.getEnvironmentEngineOrNull(level) ?: return 0

        context.source.sendSystemMessage(Component.literal("scan: ${engine.scanTaskCount}, update: ${engine.maintainTaskCount}"))

        return Command.SINGLE_SUCCESS
    }
}