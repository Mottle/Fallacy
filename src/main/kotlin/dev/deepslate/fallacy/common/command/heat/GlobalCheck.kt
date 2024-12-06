package dev.deepslate.fallacy.common.command.heat

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import dev.deepslate.fallacy.util.command.GameCommand
import net.minecraft.commands.CommandSourceStack

class GlobalCheck : GameCommand {
    override val source: String = "fallacy heat globalCheck"

    override val suggestions: Map<String, SuggestionProvider<CommandSourceStack>> = emptyMap()

    override val permissionRequired: String? = null

    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        TODO("Not yet implemented")
    }
}