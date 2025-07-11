package dev.deepslate.fallacy.command

import dev.deepslate.fallacy.command.heat.ChunkState
import dev.deepslate.fallacy.command.heat.EngineState
import dev.deepslate.fallacy.command.heat.GlobalCheck
import dev.deepslate.fallacy.command.heat.Here
import dev.deepslate.fallacy.command.heat.Query
import dev.deepslate.fallacy.command.heat.QueryState
import dev.deepslate.fallacy.command.race.RaceGet
import dev.deepslate.fallacy.command.race.RaceSet
import dev.deepslate.fallacy.command.weather.WeatherClean
import dev.deepslate.fallacy.command.weather.WeatherSet
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