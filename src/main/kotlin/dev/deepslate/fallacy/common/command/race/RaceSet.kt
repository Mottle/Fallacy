package dev.deepslate.fallacy.common.command.race

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.race.FallacyRaces
import dev.deepslate.fallacy.race.Race
import dev.deepslate.fallacy.race.impl.Unknown
import dev.deepslate.fallacy.util.command.GameCommand
import dev.deepslate.fallacy.util.command.announce.AutoloadCommand
import dev.deepslate.fallacy.util.command.suggestion.SimpleSuggestionProvider
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

@AutoloadCommand
class RaceSet : GameCommand {

    companion object {
        private val RACE_ID_SUGGESTION = SimpleSuggestionProvider { _ ->
            FallacyRaces.REGISTRY.keySet().map { it.toString().replace(':', '.') }
        }
    }

    override val source: String = "fallacy race set %s<player name> %s<race id>"

    override val permissionRequired: String? = null

    override val suggestions: Map<String, SuggestionProvider<CommandSourceStack>> = mapOf(
        "player name" to SimpleSuggestionProvider.SERVER_PLAYER_NAME,
        "race id" to RACE_ID_SUGGESTION
    )

    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        val playerName = StringArgumentType.getString(context, "player name")
        val idString = StringArgumentType.getString(context, "race id")
        val raceId = ResourceLocation.tryBySeparator(idString, '.') ?: Unknown.ID
        val player = context.getSource().server.playerList.getPlayerByName(playerName) ?: return 0

        if (!Race.contains(raceId)) {
            context.source.sendSystemMessage(Component.literal("Race $raceId not found."))
            return 0
        }

        player.setData(FallacyAttachments.RACE_ID, raceId)
        val race = Race.get(player)
        race.set(player)
        Race.sync(player)

        return Command.SINGLE_SUCCESS
    }
}