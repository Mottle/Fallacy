package dev.deepslate.fallacy.util.command.suggestion

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.SharedSuggestionProvider
import java.util.concurrent.CompletableFuture

class SimpleSuggestionProvider(val factory: (CommandContext<CommandSourceStack>) -> List<String>) :
    SuggestionProvider<CommandSourceStack> {
    override fun getSuggestions(
        context: CommandContext<CommandSourceStack>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        val list = factory(context)
        list
            .filter { SharedSuggestionProvider.matchesSubStr(builder.remaining, it) }
            .forEach(builder::suggest)

        return builder.buildFuture()
    }

    companion object {
        val SERVER_PLAYER_NAME = SimpleSuggestionProvider { it.source.server.playerNames.toList() }
    }
}