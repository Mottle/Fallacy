package dev.deepslate.fallacy.common.command.race

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.util.command.GameCommand
import dev.deepslate.fallacy.util.command.announce.AutoloadCommand
import dev.deepslate.fallacy.util.command.suggestion.ServerPlayerNameSuggestion
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component

@AutoloadCommand
class RaceGet : GameCommand {
    override val source: String = "fallacy race get %s<player name>"

    override val permissionRequired: String? = null

    override val suggestions: Map<String, SuggestionProvider<CommandSourceStack>> = mapOf(
        "player name" to ServerPlayerNameSuggestion()
    )

    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        val playerName = StringArgumentType.getString(context, "player name")
        val server = context.source.server
        val commandFrom = context.source.source
//        val player = context.source.server.profileRepository.findProfilesByNames(arrayOf("Wangyee"), object : ProfileLookupCallback {
//            override fun onProfileLookupSucceeded(profile: GameProfile?) {
//                val player = ServerPlayer()
//            }
//
//            override fun onProfileLookupFailed(profileName: String?, exception: Exception?) {
//                TODO("Not yet implemented")
//            }
//        })
//    }

        val player = server.playerList.getPlayerByName(playerName)
        if (player == null) {
            commandFrom.sendSystemMessage(Component.literal("$playerName not found."))
        } else {
            commandFrom.sendSystemMessage(Component.literal("Race: ${player.getData(FallacyAttachments.RACE_ID)}"))
        }

        return Command.SINGLE_SUCCESS
    }

}