package dev.deepslate.fallacy.common.command.race

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.deepslate.fallacy.common.command.FallacyCommand
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component

class RaceSet : FallacyCommand {
    override val command: LiteralArgumentBuilder<CommandSourceStack> =
        Commands.literal("race")
            .then(
                Commands.literal("set")
                    .then(Commands.argument("race id", StringArgumentType.word()).executes(::execute))
            )

    override val permissionRequired: String? = null

    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        val raceId = StringArgumentType.getString(context, "race id")
        context.source.player?.sendSystemMessage(Component.literal(raceId))
        return Command.SINGLE_SUCCESS
    }
}