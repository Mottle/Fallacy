package dev.deepslate.fallacy.common.capability.race

import dev.deepslate.fallacy.common.data.player.PlayerAttribute

interface IRace {
    val defaultAttribute: PlayerAttribute

    fun tick() {}

    fun apply() {}
}