package dev.deepslate.fallacy.common.command.race

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.util.announce.Autoload
import dev.deepslate.fallacy.util.command.GameCommand
import dev.deepslate.fallacy.util.command.suggestion.SimpleSuggestionProvider
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component

@Autoload
class RaceGet : GameCommand {
    override val source: String = "fallacy race get %s<player name>"

    override val permissionRequired: String? = null

    override val suggestions: Map<String, SuggestionProvider<CommandSourceStack>> = mapOf(
        "player name" to SimpleSuggestionProvider.SERVER_PLAYER_NAME
    )

    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        val playerName = StringArgumentType.getString(context, "player name")
        val server = context.source.server
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
            context.source.sendSuccess({ Component.literal("$playerName not found.") }, true)
        } else {
            context.source.sendSuccess(
                { Component.literal("Race: ${player.getData(FallacyAttachments.RACE_ID)}.") },
                true
            )
        }

        return Command.SINGLE_SUCCESS
    }

}