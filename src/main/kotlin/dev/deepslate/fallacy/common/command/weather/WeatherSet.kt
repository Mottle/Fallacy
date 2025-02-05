package dev.deepslate.fallacy.common.command.weather

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import dev.deepslate.fallacy.util.command.GameCommand
import dev.deepslate.fallacy.util.command.suggestion.SimpleSuggestionProvider
import dev.deepslate.fallacy.util.extension.weatherEngine
import dev.deepslate.fallacy.util.region.UniversalRegion
import dev.deepslate.fallacy.weather.FallacyWeathers
import dev.deepslate.fallacy.weather.WeatherInstance
import dev.deepslate.fallacy.weather.impl.ServerWeatherEngine
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.ResourceLocationArgument

class WeatherSet : GameCommand {
    override val source: String = "fallacy weather set %r<weather>"

    override val suggestions: Map<String, SuggestionProvider<CommandSourceStack>>
        get() = mapOf("weather" to SimpleSuggestionProvider.WEATHER)

    override val permissionRequired: String? = null

    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        val weatherId = ResourceLocationArgument.getId(context, "weather")
        val weather = FallacyWeathers.REGISTRY.get(weatherId) ?: return 0
        val engine = (context.source.level.weatherEngine ?: return 0) as ServerWeatherEngine

        val instance = WeatherInstance(weather, region = UniversalRegion, priority = 7)

        engine.addWeather(instance)
        engine.markDirty()
        return 1
    }
}