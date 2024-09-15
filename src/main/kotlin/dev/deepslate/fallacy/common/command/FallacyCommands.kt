package dev.deepslate.fallacy.common.command

import dev.deepslate.fallacy.util.Loader
import dev.deepslate.fallacy.util.command.GameCommand

object FallacyCommands {

    private val subcommands: ArrayList<GameCommand> = arrayListOf()

    val commands: List<GameCommand> get() = subcommands.toList()

    internal fun add(command: GameCommand) {
        subcommands.add(command)
    }

    init {
//        Loader().loadAllCommands()
        Loader().load<GameCommand> { add(it) }
    }
}