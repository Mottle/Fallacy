package dev.deepslate.fallacy.weather.current

import dev.deepslate.fallacy.util.TickHelper
import dev.deepslate.fallacy.util.extension.weatherEngine
import dev.deepslate.fallacy.util.region.ChunkRegion
import dev.deepslate.fallacy.util.region.CubeRegion
import dev.deepslate.fallacy.weather.FallacyWeathers
import dev.deepslate.fallacy.weather.ServerWeatherEngine
import dev.deepslate.fallacy.weather.WeatherInstance
import dev.deepslate.fallacy.weather.wind.ServerWindEngine
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.phys.Vec3
import kotlin.math.cos
import kotlin.math.sin

class RandomCurrentSimulator(val serverLevel: ServerLevel) : CurrentSimulator {

    companion object {
        const val MAX_WEATHER_SIZE = 100

        const val DEFAULT_GLOBAL_WIND_STRENGTH = 0.04

        const val WEATHER_GENERATION_SIZE = 20
    }

    val globalRegion = CubeRegion(0, 60, 0, 20000, 256, 20000)

    override val weatherEngine: ServerWeatherEngine
        get() = serverLevel.weatherEngine!! as ServerWeatherEngine

    override val windEngine: ServerWindEngine
        get() = weatherEngine.windEngine

    override fun tick() {
        if (!TickHelper.checkServerMinuteRate(5)) return
        if (serverLevel.weatherEngine == null) return

        tickWind()
        weatherEngine.clean()

        if (weatherEngine.size >= MAX_WEATHER_SIZE) return

        val weatherInstances = rollPosition().map(::generateWeather)

        feedWeather(weatherEngine, weatherInstances)
    }

    var globalWindAngle: Int = 0

    var globalWindStrength: Double = DEFAULT_GLOBAL_WIND_STRENGTH

    private fun tickWind() {
        //很显然需要一个全局时间管理器替代服务器时间
        if (TickHelper.checkServerMinuteRate(20)) return

        globalWindAngle = (globalWindAngle + 1) % 360
        val rad = Math.toRadians(globalWindAngle.toDouble())
        val vec = Vec3(cos(rad), 0.0, -sin(rad)).scale(globalWindStrength)
        windEngine.globalWindVec = vec
    }

    private fun rollPosition(): List<BlockPos> =
        chooseRandom().filter { serverLevel.random.nextFloat() >= 0.75f }

    private fun generateWeather(blockPos: BlockPos): WeatherInstance {
        val random = serverLevel.random
        val sec = random.nextInt(5, 40)
        val radius = random.nextIntBetweenInclusive(4, 9)
        val chunkCenter = ChunkPos(blockPos)
        val chunkStart = ChunkPos(chunkCenter.x - radius, chunkCenter.z - radius)
        val chunkEnd = ChunkPos(chunkCenter.x + radius, chunkCenter.z + radius)
        val region = ChunkRegion(chunkStart, chunkEnd)

        return WeatherInstance.create(FallacyWeathers.RAIN, sec, region)
    }

    private fun chooseRandom(): List<BlockPos> = List(WEATHER_GENERATION_SIZE) {
        globalRegion.randomPos(serverLevel)
    }

    private fun feedWeather(weatherEngine: ServerWeatherEngine, weathers: List<WeatherInstance>) {
        weathers.forEach { weatherEngine.addWeather(it) }
        weatherEngine.markDirty()
    }
}