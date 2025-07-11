package dev.deepslate.fallacy.command.weather

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import dev.deepslate.fallacy.util.command.GameCommand
import dev.deepslate.fallacy.util.extension.weatherEngine
import dev.deepslate.fallacy.weather.impl.ServerWeatherEngine
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component

class WeatherClean : GameCommand {
    override val source: String = "fallacy weather clean"

    override val suggestions: Map<String, SuggestionProvider<CommandSourceStack>> = mapOf()

    override val permissionRequired: String? = "fallacy.command.weather.clean"

    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        val engine = context.source.level.weatherEngine as? ServerWeatherEngine ?: return 0

        engine.removeAll()
        engine.markDirty()

        context.source.sendSuccess({ Component.literal("Weather cleaned.") }, true)

        return Command.SINGLE_SUCCESS
    }
}