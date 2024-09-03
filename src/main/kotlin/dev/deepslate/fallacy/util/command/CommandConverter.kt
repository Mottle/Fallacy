package dev.deepslate.fallacy.util.command

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import dev.deepslate.fallacy.util.command.lexer.CommandLexer
import net.minecraft.commands.CommandSourceStack

class CommandConverter {

    private val lexer = CommandLexer()

    private val builder = CommandBuilder()

    fun convert(command: GameCommand): LiteralArgumentBuilder<CommandSourceStack> {
        val tokens = lexer.scan(command.source)
        val rawCommand = builder.fromTokens(tokens, command::execute, command.suggestions)
        return rawCommand
    }
}