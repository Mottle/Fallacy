package dev.deepslate.fallacy.weather

interface WeatherStorage {
    val weatherStorage: Collection<WeatherInstance>

    val sortedWeathers: List<WeatherInstance>
        get() = weatherStorage.toList()

    val size: Int
        get() = weatherStorage.size
}