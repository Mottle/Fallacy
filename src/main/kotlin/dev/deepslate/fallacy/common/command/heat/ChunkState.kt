package dev.deepslate.fallacy.common.command.heat

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.util.command.GameCommand
import net.minecraft.commands.CommandSourceStack

class ChunkState : GameCommand {
    override val source: String = "fallacy heat chunk_state"

    override val suggestions: Map<String, SuggestionProvider<CommandSourceStack>> = emptyMap()

    override val permissionRequired: String? = "fallacy.command.heat.chunk_state"

    override fun execute(context: CommandContext<CommandSourceStack>): Int {
        if (context.source.player == null) return 0
        val player = context.source.player!!
        val blockPos = player.blockPosition()
        val chunk = player.level().getChunkAt(blockPos)
        val state = chunk.getData(FallacyAttachments.HEAT_PROCESS_STATE)
        context.source.sendSystemMessage(state.toComponent())

        return Command.SINGLE_SUCCESS
    }
}