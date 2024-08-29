package dev.deepslate.fallacy.common.capability.race

import dev.deepslate.fallacy.common.data.player.PlayerAttribute

object Unknown : IRace {
    override val defaultAttribute: PlayerAttribute = PlayerAttribute()
}