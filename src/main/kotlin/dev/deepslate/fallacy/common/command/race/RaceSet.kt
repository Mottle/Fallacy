package dev.deepslate.fallacy.common.command.race

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.util.command.GameCommand
import dev.deepslate.fallacy.util.command.announce.AutoloadCommand
import dev.deepslate.fallacy.util.command.suggestion.SimpleSuggestionProvider
import net.minecraft.commands.CommandSourceStack

@AutoloadCommand
class RaceSet : GameCommand {
    override val source: String = "fallacy race set %s<player name> %s<race id>"

    override val permissionRequired: String? = null

    override val suggestions: Map<String, SuggestionProvider<CommandSourceStack>> = mapOf(
        "player name" to SimpleSuggestionProvider.SERVER_PLAYER_NAME
    )

    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        val playerName = StringArgumentType.getString(context, "player name")
        val raceId = StringArgumentType.getString(context, "race id")
        val player = context.getSource().server.playerList.getPlayerByName(playerName) ?: return 0

        player.setData(FallacyAttachments.RACE_ID, Fallacy.id(raceId))
        return Command.SINGLE_SUCCESS
    }
}