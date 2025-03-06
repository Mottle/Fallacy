package dev.deepslate.fallacy.common.command.test

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import dev.deepslate.fallacy.util.announce.Autoload
import dev.deepslate.fallacy.util.command.GameCommand
import io.netty.channel.embedded.EmbeddedChannel
import net.minecraft.commands.CommandSourceStack
import net.minecraft.core.UUIDUtil
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.network.CommonListenerCookie
import net.neoforged.neoforge.common.util.FakePlayerFactory

@Autoload
class Fake : GameCommand {
    override val source: String = "fallacy test fake"

    override val suggestions: Map<String, SuggestionProvider<CommandSourceStack>> = mapOf()

    override val permissionRequired: String? = "fallacy.command.test.fake"

    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        val player = context.source.player ?: return 0
        val level = player.level() as ServerLevel
        val server = player.server
        val profiler = UUIDUtil.createOfflineProfile("fake_tester")
        val fakePlayer = FakePlayerFactory.get(level, profiler)
        val cookie = CommonListenerCookie.createInitial(profiler, false)

        val connection = fakePlayer.connection.connection
        EmbeddedChannel(connection)

        fakePlayer.setPos(player.position())
        server.playerList.placeNewPlayer(connection, fakePlayer, cookie)

        return Command.SINGLE_SUCCESS
    }
}