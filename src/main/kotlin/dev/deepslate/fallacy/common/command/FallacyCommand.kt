package dev.deepslate.fallacy.common.command

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack

interface FallacyCommand {
    val command: LiteralArgumentBuilder<CommandSourceStack>

    val permissionRequired: String?

    fun execute(context: CommandContext<CommandSourceStack>): Int
}