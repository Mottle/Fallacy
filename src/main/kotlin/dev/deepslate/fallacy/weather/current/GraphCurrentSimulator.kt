package dev.deepslate.fallacy.weather.current

import dev.deepslate.fallacy.util.graph.Graph
import dev.deepslate.fallacy.weather.impl.ServerWeatherEngine
import dev.deepslate.fallacy.weather.impl.ServerWindEngine

class GraphCurrentSimulator : CurrentSimulator {

    val graph: Graph<Int, Int> = Graph()

    override fun tick() {
        TODO("Not yet implemented")
    }

    override val weatherEngine: ServerWeatherEngine
        get() = TODO("Not yet implemented")

    override val windEngine: ServerWindEngine
        get() = TODO("Not yet implemented")
}