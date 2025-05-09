package dev.deepslate.fallacy.common.command

import dev.deepslate.fallacy.common.command.heat.*
import dev.deepslate.fallacy.common.command.race.RaceGet
import dev.deepslate.fallacy.common.command.race.RaceSet
import dev.deepslate.fallacy.common.command.weather.WeatherClean
import dev.deepslate.fallacy.common.command.weather.WeatherSet
import dev.deepslate.fallacy.util.command.GameCommand

object FallacyCommands {

    private val subcommands: ArrayList<GameCommand> = arrayListOf()

    val commands: List<GameCommand> get() = subcommands.toList()

    internal fun add(vararg command: GameCommand) {
        subcommands += command
    }

    init {
//        Loader().loadAllCommands()
//        Loader("Command").load<GameCommand> { add(it) }
        add(RaceGet())
        add(RaceSet())
        add(GlobalCheck())
        add(EngineState())
        add(ChunkState())
        add(WeatherSet())
        add(WeatherClean())
        add(Here())
        add(QueryState())
        add(Query())
//        add(Fake())
    }
}