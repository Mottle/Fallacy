package dev.deepslate.fallacy.util.command.lexer

internal sealed interface TokenType {
    data class Word(val word: String) : TokenType

    sealed class Arg(open val word: String) : TokenType

    data class StringArg(override val word: String) : Arg(word)

    data class IntArg(override val word: String) : Arg(word)

    data class FloatArg(override val word: String) : Arg(word)

    data class BoolArg(override val word: String) : Arg(word)
}