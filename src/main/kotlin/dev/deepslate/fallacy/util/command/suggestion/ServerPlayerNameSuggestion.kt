package dev.deepslate.fallacy.util.command.suggestion

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.commands.CommandSourceStack
import java.util.concurrent.CompletableFuture

class ServerPlayerNameSuggestion : SuggestionProvider<CommandSourceStack> {
    override fun getSuggestions(
        context: CommandContext<CommandSourceStack>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        val server = context.source.server
        val onlinePlayerNames = server.playerNames.toList()
//        if(onlinePlayerNames.contains(input)) return Suggestions.empty()

        onlinePlayerNames
            .filter { it.endsWith(builder.remaining) }
            .forEach(builder::suggest)

        return builder.buildFuture()
    }
}