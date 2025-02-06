package dev.deepslate.fallacy.common.command.heat

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.util.command.GameCommand
import net.minecraft.commands.CommandSourceStack
import net.minecraft.core.BlockPos

class QueryState : GameCommand {
    override val source: String = "fallacy heat query_state %i<x> %i<z>"

    override val suggestions: Map<String, SuggestionProvider<CommandSourceStack>> = emptyMap()

    override val permissionRequired: String? = null

    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        val x = IntegerArgumentType.getInteger(context, "x")
        val z = IntegerArgumentType.getInteger(context, "z")

        val chunk = context.source.level.getChunk(BlockPos(x, 0, z)) ?: return 0
        val state = chunk.getData(FallacyAttachments.HEAT_PROCESS_STATE)
        context.source.sendSuccess({ state.toComponent() }, true)

        return Command.SINGLE_SUCCESS
    }
}