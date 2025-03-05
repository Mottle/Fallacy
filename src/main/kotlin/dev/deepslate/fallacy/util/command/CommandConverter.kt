package dev.deepslate.fallacy.util.command

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.deepslate.fallacy.util.command.lexer.CommandLexer
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.node.types.PermissionNode
import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.level.ServerPlayer

class CommandConverter {

    private val lexer = CommandLexer()

    private val builder = CommandBuilder()

    fun convert(command: GameCommand): LiteralArgumentBuilder<CommandSourceStack> {
        val tokens = lexer.scan(command.source)

        val execution = execution@{ context: CommandContext<CommandSourceStack> ->
            if (context.source.player != null && command.permissionRequired != null) {
                val player = context.source.player!!
                val luckPerms = LuckPermsProvider.get()
                val user = luckPerms.getPlayerAdapter<ServerPlayer>(ServerPlayer::class.java).getUser(player)
                val permission = PermissionNode.builder(command.permissionRequired!!).build()

                if (!user.distinctNodes.contains(permission)) return@execution 0
            }
            return@execution command.execute(context)
        }

        val rawCommand = builder.fromTokens(tokens, execution, command.suggestions)
        return rawCommand
    }
}