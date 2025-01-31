package dev.deepslate.fallacy.thermodynamics

import com.mojang.serialization.Codec
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component

enum class HeatProcessState {
    CORRECTED, PENDING, ERROR, UNPROCESSED;

    companion object {
        val CODEC: Codec<HeatProcessState> = Codec.STRING.xmap(HeatProcessState::valueOf, HeatProcessState::name)


        private val corrected = Component.literal("Corrected").withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN)

        private val pending = Component.literal("Pending").withStyle(ChatFormatting.BOLD, ChatFormatting.YELLOW)

        private val error = Component.literal("Error").withStyle(ChatFormatting.BOLD, ChatFormatting.RED)

        private val unprocessed = Component.literal("Unprocessed").withStyle(ChatFormatting.BOLD, ChatFormatting.GRAY)
    }

    fun toComponent(): Component = when (this) {
        CORRECTED -> Component.empty().append(corrected.copy()).append(Component.literal("|Pending|Error|Unprocessed").withStyle(ChatFormatting.GRAY))

        PENDING -> Component.literal("Corrected|").withStyle(ChatFormatting.GRAY).append(pending)
            .append(Component.literal("|Error|Unprocessed|").withStyle(ChatFormatting.GRAY))

        ERROR -> Component.literal("Corrected|Pending|").withStyle(ChatFormatting.GRAY).append(error)
            .append(Component.literal("|Unprocessed").withStyle(ChatFormatting.GRAY))

        UNPROCESSED -> Component.literal("Corrected|Pending|Error|").withStyle(ChatFormatting.GRAY).append(unprocessed)
    }
}