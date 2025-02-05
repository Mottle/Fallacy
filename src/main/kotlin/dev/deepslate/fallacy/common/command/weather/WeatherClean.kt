package dev.deepslate.fallacy.common.command.weather

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import dev.deepslate.fallacy.util.command.GameCommand
import dev.deepslate.fallacy.util.extension.weatherEngine
import dev.deepslate.fallacy.weather.impl.ServerWeatherEngine
import net.minecraft.commands.CommandSourceStack

class WeatherClean : GameCommand {
    override val source: String = "fallacy weather clean"

    override val suggestions: Map<String, SuggestionProvider<CommandSourceStack>> = mapOf()

    override val permissionRequired: String? = null

    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        ((context.source.level.weatherEngine ?: return 0) as ServerWeatherEngine).removeAll()
        return 1
    }
}