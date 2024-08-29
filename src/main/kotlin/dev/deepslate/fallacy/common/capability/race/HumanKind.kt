package dev.deepslate.fallacy.common.capability.race

import dev.deepslate.fallacy.common.data.player.PlayerAttribute

class HumanKind : IRace {

    companion object {
        val ATTRIBUTE = PlayerAttribute()
    }

    override val defaultAttribute: PlayerAttribute = ATTRIBUTE

    override fun tick() {}
}