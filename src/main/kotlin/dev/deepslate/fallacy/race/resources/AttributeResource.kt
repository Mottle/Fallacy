package dev.deepslate.fallacy.race.resources

import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import dev.deepslate.fallacy.race.Race
import net.minecraft.world.entity.player.Player

class AttributeResource(val attribute: PlayerAttribute) : Race.Resource {
    companion object {
        const val KEY = "attribute"
    }

    override fun apply(player: Player) {
        attribute.set(player, true)
    }

    override fun deapply(player: Player) {}
}