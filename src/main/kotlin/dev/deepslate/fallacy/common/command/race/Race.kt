package dev.deepslate.fallacy.common.command.race

import com.mojang.brigadier.Command
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import dev.deepslate.fallacy.common.command.FallacyCommand
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component

class Race : FallacyCommand {

    companion object {
        private val MESSAGE = Component.translatable("fallacy.command.message.race")
    }

    override val command: LiteralArgumentBuilder<CommandSourceStack> = Commands.literal("race").executes(::execute)

    override val permissionRequired: String? = null

    override fun execute(context: CommandContext<CommandSourceStack>): Int {

        context.source.player?.sendSystemMessage(MESSAGE)

        return Command.SINGLE_SUCCESS
    }
}