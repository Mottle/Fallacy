package dev.deepslate.fallacy.client.fog

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.weather.Weather
import dev.deepslate.fallacy.weather.Weathers
import dev.deepslate.fallacy.weather.impl.ClientWeatherEngine
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.FogRenderer
import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.material.MapColor
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import net.neoforged.neoforge.client.event.ViewportEvent
import org.apache.logging.log4j.LogManager

@OnlyIn(Dist.CLIENT)
class FogAdjuster {
    companion object {
        private val logger = LogManager.getLogger("fallacy-fog-adjuster")

        private val SANDSTORM get() = FogState(Triple(0.7F, 0.5F, 0.2F), 0f, 18f)

        private val SNOWSTORM get() = FogState(Triple(0.7F, 0.7F, 0.7F), 0f, 20f)

        private val HEATWAVE get() = FogState(Triple(0.5F, 0.2F, 0.1F), 0f, 75f)

        private val DEFAULT get() = FogState(Triple(-1f, -1f, -1f), -1f, -1f)
    }

    private val vanilla = DEFAULT.copy()

    private var target: FogState = DEFAULT.copy()

    private var active: FogState = DEFAULT.copy()

    private val delta: FogState = FogState.zero()

    private var lastTickWeather: Weather? = null

    private var firstUsedInit = false

    private var lerpTicksCurrent = 20 * 4

    private val lerpTicksMax = 20 * 4

    private var farFog = false

    fun isFogOverriding(): Boolean {
        val mc = Minecraft.getInstance()
        val blockAtCamera = mc.gameRenderer.mainCamera.blockAtCamera

        if (blockAtCamera.block.defaultMapColor() == MapColor.WATER) return false

        val weather = ClientWeatherEngine.weatherHere?.weather ?: return false

        return weather == Weathers.SANDSTORM.get() || weather == Weathers.SNOWSTORM.get() || lerpTicksCurrent < lerpTicksMax
    }

    fun onFogColor(event: ViewportEvent.ComputeFogColor) {
//        val weather = ClientWeatherEngine.weatherHere?.weather ?: Weathers.CLEAR.get()
//
//        if (weather != lastTickWeather) {
//            lastTickWeather = weather
//            setupWeatherFog(weather)
//        }

        vanilla.rgb = Triple(event.red, event.green, event.blue)

        if (isFogOverriding() && active != DEFAULT) {
            val level = Minecraft.getInstance().level ?: return
            val brightness =
                Mth.clamp(Mth.cos(level.getTimeOfDay(1f) * (Math.PI.toFloat() * 2f)) * 2.0f + 0.5f, 0.0f, 1.0f)
            event.red = active.rgb.first * brightness
            event.green = active.rgb.second * brightness
            event.blue = active.rgb.third * brightness
            Fallacy.LOGGER.info("red: ${event.red}, green: ${event.green}, blue: ${event.blue}")
        }
    }

    fun onFogRender(event: ViewportEvent.RenderFog) {
//        val weather = ClientWeatherEngine.weatherHere?.weather ?: Weathers.CLEAR.get()
//
//        if (weather != lastTickWeather) {
//            lastTickWeather = weather
//            setupWeatherFog(weather)
//        }

        if (event.mode == FogRenderer.FogMode.FOG_SKY) {
            vanilla.skyStart = event.nearPlaneDistance
            vanilla.skyEnd = event.farPlaneDistance
        } else {
            vanilla.fogStart = event.nearPlaneDistance
            vanilla.fogEnd = event.farPlaneDistance
        }

        if (isFogOverriding() && active != DEFAULT) {
            if (event.mode == FogRenderer.FogMode.FOG_SKY) {
                event.nearPlaneDistance = active.skyStart
                event.farPlaneDistance = active.skyEnd
//                Fallacy.LOGGER.info("skyStart: ${active.skyStart}, skyEnd: ${active.skyEnd}")
                event.setCanceled(true)
            } else {
                event.nearPlaneDistance = active.fogStart
                event.farPlaneDistance = active.fogEnd
//                Fallacy.LOGGER.info("fogStart: ${active.fogStart}, fogEnd: ${active.fogEnd}")
                event.setCanceled(true)
            }
        }
    }

    private var checkedTick = 0

    fun tick(weather: Weather) {
        ++checkedTick
        if (checkedTick == 5) checkedTick = 0

        if (lastTickWeather != weather) {
            lastTickWeather = weather
            setupWeatherFog(weather)
        }

        if (Weathers.SANDSTORM.value() == weather || Weathers.SNOWSTORM.value() == weather) {
            val player = Minecraft.getInstance().player

            if (player != null && checkedTick == 0) {
                val playerOutside = player.isInWater || isOutside(player)
                val farFog = playerOutside || player.isSpectator

                if (farFog != this.farFog) {
                    this.farFog = farFog
                    if (Weathers.SANDSTORM.value() == weather) {
                        setupSandstormFog(farFog)
                    } else {
                        setupSnowstormFog(farFog)
                    }
                }
            }
        }


        if (lerpTicksCurrent < lerpTicksMax) {
            val newLerpX = active.rgb.first + delta.rgb.first
            val newLerpY = active.rgb.second + delta.rgb.second
            val newLerpZ = active.rgb.third + delta.rgb.third

            active.rgb = Triple(newLerpX, newLerpY, newLerpZ)
            active.fogStart = active.fogStart + delta.fogStart
            active.fogEnd = active.fogEnd + delta.fogEnd
            active.skyStart = active.skyStart + delta.skyStart
            active.skyEnd = active.skyEnd + delta.skyEnd
            lerpTicksCurrent++
        } else active = target.copy()
    }

    fun isOutside(player: Player): Boolean {
        val pos = player.blockPosition()
        for (x in pos.x - 2..pos.x + 2) for (z in pos.z - 2..pos.z + 2) {
            val checkPos = BlockPos(x, pos.y, z)

            if (player.level().canSeeSky(checkPos)) return false
        }
        return true
    }

    fun setupWeatherFog(weather: Weather) {
        when (weather) {
            Weathers.SANDSTORM.value() -> setupSandstormFog(false)
            Weathers.SNOWSTORM.value() -> setupSnowstormFog(false)
            else -> setupClear()
        }
    }

    fun setupSandstormFog(farFog: Boolean) {
        logger.info("Set up sandstorm fog.")
        val distanceAmplifier = if (!farFog) 1f else 4f
        target = SANDSTORM.copy(fogEnd = SANDSTORM.fogEnd * distanceAmplifier)
        setupLerpRate()
    }

    fun setupSnowstormFog(farFog: Boolean) {
        logger.info("Set up snowstorm fog.")
        val distanceAmplifier = if (farFog) 1f else 4f
        target = SNOWSTORM.copy(fogEnd = SNOWSTORM.fogEnd * distanceAmplifier)
        setupLerpRate()
    }

    fun setupClear() {
        logger.info("Set up clear fog.")
        target = vanilla.copy()
        setupLerpRate()
    }

    fun setupLerpRate() {
        if (!firstUsedInit) {
            if (vanilla.fogEnd != -1f && vanilla.skyEnd != -1f) {
                active = vanilla.copy()
                firstUsedInit = true
            }
        }

        lerpTicksCurrent = 0
        val partialLerpX = getLerpRate(active.rgb.first, target.rgb.first, lerpTicksMax.toFloat())
        val partialLerpY = getLerpRate(active.rgb.second, target.rgb.second, lerpTicksMax.toFloat())
        val partialLerpZ = getLerpRate(active.rgb.third, target.rgb.third, lerpTicksMax.toFloat())

        delta.rgb = Triple(partialLerpX, partialLerpY, partialLerpZ)
        delta.fogStart = getLerpRate(active.fogStart, target.fogStart, lerpTicksMax.toFloat())
        delta.fogEnd = getLerpRate(active.fogEnd, target.fogEnd, lerpTicksMax.toFloat())
        delta.skyStart = getLerpRate(active.skyStart, target.skyStart, lerpTicksMax.toFloat())
        delta.skyEnd = getLerpRate(active.skyEnd, target.skyEnd, lerpTicksMax.toFloat())
    }

    private fun getLerpRate(current: Float, end: Float, fullLerpTicks: Float) = (end - current) / fullLerpTicks
}