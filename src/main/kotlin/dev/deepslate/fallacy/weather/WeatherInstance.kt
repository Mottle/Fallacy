package dev.deepslate.fallacy.weather

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.deepslate.fallacy.util.TickHelper
import dev.deepslate.fallacy.util.region.Region
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.phys.Vec3
import java.util.Optional
import kotlin.jvm.optionals.getOrNull

class WeatherInstance(
    val weather: Weather,
    private var remainingTicks: Int = TickHelper.minute(10),
    val region: Region,
    weatherEntityPos: Vec3? = null
) {
    companion object {
        val CODEC = RecordCodecBuilder.create<WeatherInstance> { instance ->
            instance.group(
                ResourceLocation.CODEC.fieldOf("weather").forGetter { it.weather.namespaceId },
                Codec.INT.fieldOf("remaining_ticks").forGetter(WeatherInstance::remainingTicks),
                Region.CODEC.fieldOf("region").forGetter(WeatherInstance::region),
                Vec3.CODEC.optionalFieldOf("weather_entity_pos")
                    .forGetter { Optional.ofNullable(it.weatherEntity?.position()) }
            ).apply(instance) { weatherId, remainingTicks, region, pos ->
                WeatherInstance(
                    FallacyWeathers.REGISTRY.get(weatherId)!!,
                    remainingTicks,
                    region,
                    pos.getOrNull()
                )
            }
        }
    }

    fun isIn(pos: BlockPos) = region.isIn(pos)

    val isEnded: Boolean
        get() = remainingTicks <= 0

    val weatherEntity = weather.createWeatherEntity()

    init {
        if (weatherEntityPos != null && weatherEntity != null) weatherEntity.setPos(weatherEntityPos)
    }

    fun tick(level: ServerLevel) {
        if (remainingTicks <= 0) return

        remainingTicks--

        if (TickHelper.checkServerTickRate(weather.tickInterval)) weather.tick(level, region)

        if (weatherEntity != null && TickHelper.checkServerTickRate(weather.tickWeatherEntityInterval)) weather.tickWeatherEntity(
            weatherEntity,
            level,
            weatherEntity.blockPosition()
        )

        if (TickHelper.checkServerTickRate(weather.tickEntityInterval)) {
            val entitiesInRegion = level.entities.all.filter { region.isIn(it.blockPosition()) }
            entitiesInRegion.forEach { weather.tickEntity(it, level, it.blockPosition()) }
        }
    }
}