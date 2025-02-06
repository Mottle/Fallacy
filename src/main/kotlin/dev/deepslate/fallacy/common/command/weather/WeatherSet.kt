package dev.deepslate.fallacy.common.command.weather

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import dev.deepslate.fallacy.util.command.GameCommand
import dev.deepslate.fallacy.util.command.suggestion.SimpleSuggestionProvider
import dev.deepslate.fallacy.util.extension.weatherEngine
import dev.deepslate.fallacy.util.region.ChunkRegion
import dev.deepslate.fallacy.util.region.UniversalRegion
import dev.deepslate.fallacy.weather.FallacyWeathers
import dev.deepslate.fallacy.weather.WeatherInstance
import dev.deepslate.fallacy.weather.impl.ServerWeatherEngine
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.ResourceLocationArgument
import net.minecraft.network.chat.Component
import net.minecraft.world.level.ChunkPos

class WeatherSet : GameCommand {
    override val source: String = "fallacy weather set %r<weather> %s<range>"

    override val suggestions: Map<String, SuggestionProvider<CommandSourceStack>>
        get() = mapOf("weather" to SimpleSuggestionProvider.WEATHER, "range" to SimpleSuggestionProvider.WEATHER_RANGE)

    override val permissionRequired: String? = null

    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        val weatherId = ResourceLocationArgument.getId(context, "weather")
        val weather = FallacyWeathers.REGISTRY.get(weatherId) ?: return 0
        val range = StringArgumentType.getString(context, "range") ?: "global"
        val region = if (range == "global") UniversalRegion else {
            val player = context.source.player ?: return 0
            val center = player.chunkPosition()
            val startChunkPos = ChunkPos(center.x - 4, center.z - 4)
            val endChunkPos = ChunkPos(center.x + 4, center.z + 4)
            ChunkRegion(startChunkPos, endChunkPos)
        }

        val engine = (context.source.level.weatherEngine ?: return 0) as ServerWeatherEngine
        val instance = WeatherInstance(weather, region = region, priority = 7)

        engine.addWeather(instance)
        engine.markDirty()

        context.source.sendSuccess({ Component.literal("Weather set.") }, false)

        return Command.SINGLE_SUCCESS
    }
}