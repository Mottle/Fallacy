package dev.deepslate.fallacy.weather

import dev.deepslate.fallacy.util.TickHelper
import dev.deepslate.fallacy.util.region.Region
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel

class WeatherInstance(
    val weather: Weather,
    private var remainingTicks: Int = TickHelper.minute(10),
    val region: Region
) {
    fun isIn(pos: BlockPos) = region.isIn(pos)

    fun isEnded() = remainingTicks <= 0

    val weatherEntity = weather.getWeatherEntity()

    fun tick(level: ServerLevel) {
        if (remainingTicks <= 0) return

        remainingTicks--

        if (TickHelper.checkServerTickRate(weather.tickInterval)) weather.tick()
        if (weatherEntity != null && TickHelper.checkServerTickRate(weather.tickWeatherEntityInterval)) weather.tickEntity(
            weatherEntity,
            level,
            weatherEntity.blockPosition()
        )
        if (TickHelper.checkServerTickRate(weather.tickEntityInterval)) {
            val entitiesInRegion = level.entities.all.filter { region.isIn(it.blockPosition()) }
            entitiesInRegion.forEach { weather.tickEntity(it, level, it.blockPosition()) }
        }

        if (TickHelper.checkServerTickRate(weather.randomTickRegionInterval)) {
            val randomCount = level.random.nextIntBetweenInclusive(
                weather.randomTickStateCountRange.first,
                weather.randomTickStateCountRange.last
            )
            val posList = (1..randomCount).map { region.randomPos(level.random) }
            val pairs = posList.map { it to level.getBlockState(it) }

            pairs.forEach { (pos, state) -> weather.randomTickRegion(state, level, pos) }
        }
    }
}