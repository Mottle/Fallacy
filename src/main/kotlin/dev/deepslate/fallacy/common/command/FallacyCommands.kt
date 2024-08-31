package dev.deepslate.fallacy.common.command

import dev.deepslate.fallacy.Fallacy
import net.minecraft.commands.Commands

object FallacyCommands {
    private val COMMAND_PREFIX = Fallacy.MOD_ID.lowercase()

    private val subcommands: ArrayList<FallacyCommand> = arrayListOf()

    val commands: List<FallacyCommand> get() = subcommands.toList()

    fun add(command: FallacyCommand) {
        subcommands.add(command)
    }

    internal fun getPrefix() = Commands.literal(COMMAND_PREFIX)

    init {
        Loader().loadAllCommands()
    }
}