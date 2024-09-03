package dev.deepslate.fallacy.util.command

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import net.minecraft.commands.CommandSourceStack

interface GameCommand {
    val source: String

    val suggestions: Map<String, SuggestionProvider<CommandSourceStack>>

    val permissionRequired: String?

    fun execute(context: CommandContext<CommandSourceStack>): Int
}