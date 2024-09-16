package dev.deepslate.fallacy.common.command.race

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import dev.deepslate.fallacy.race.Race
import dev.deepslate.fallacy.race.impl.Unknown
import dev.deepslate.fallacy.util.announce.Autoload
import dev.deepslate.fallacy.util.command.GameCommand
import dev.deepslate.fallacy.util.command.suggestion.SimpleSuggestionProvider
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

@Autoload
class RaceSet : GameCommand {

    override val source: String = "fallacy race set %s<player name> %s<race id>"

    override val permissionRequired: String? = null

    override val suggestions: Map<String, SuggestionProvider<CommandSourceStack>> = mapOf(
        "player name" to SimpleSuggestionProvider.SERVER_PLAYER_NAME,
        "race id" to SimpleSuggestionProvider.RACE_ID
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

        val newRace = Race.getOrUnknown(raceId)
        Race.setNewRace(player, newRace)

//        player.setData(FallacyAttachments.RACE_ID, raceId)
//        val race = Race.get(player)
//        race.set(player)
//        Race.sync(player)

        return Command.SINGLE_SUCCESS
    }
}