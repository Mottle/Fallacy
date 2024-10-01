package dev.deepslate.fallacy.common.command

import dev.deepslate.fallacy.common.command.race.RaceGet
import dev.deepslate.fallacy.common.command.race.RaceSet
import dev.deepslate.fallacy.util.command.GameCommand

object FallacyCommands {

    private val subcommands: ArrayList<GameCommand> = arrayListOf()

    val commands: List<GameCommand> get() = subcommands.toList()

    internal fun add(command: GameCommand) {
        subcommands.add(command)
    }

    init {
//        Loader().loadAllCommands()
//        Loader("Command").load<GameCommand> { add(it) }
        add(RaceGet())
        add(RaceSet())
    }
}