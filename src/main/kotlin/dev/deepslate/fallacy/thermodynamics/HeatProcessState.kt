package dev.deepslate.fallacy.thermodynamics

import com.mojang.serialization.Codec

enum class HeatProcessState {
    CORRECTED, PENDING, ERROR, UNPROCESSED;

    companion object {
        val CODEC: Codec<HeatProcessState> = Codec.STRING.xmap(HeatProcessState::valueOf, HeatProcessState::name)
    }
}