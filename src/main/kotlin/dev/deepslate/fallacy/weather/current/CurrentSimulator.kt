package dev.deepslate.fallacy.weather.current

import dev.deepslate.fallacy.weather.ServerWeatherEngine
import dev.deepslate.fallacy.weather.wind.ServerWindEngine

//用于模拟真实气候环流来生成天气
interface CurrentSimulator {
    fun tick()

    val weatherEngine: ServerWeatherEngine

    val windEngine: ServerWindEngine
}