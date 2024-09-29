package dev.deepslate.fallacy.race.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.player.NutritionState
import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import dev.deepslate.fallacy.race.Race
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer

class Orc : Race {

    companion object {
        val ID = Fallacy.id("orc")
    }

    override val namespacedId: ResourceLocation = ID

    override val attribute: PlayerAttribute =
        PlayerAttribute(health = 40.0, attackDamage = 4.0, armor = 6.0, magicResistance = 10.0, strength = 3.0)

    override val nutrition: NutritionState = NutritionState()

    override fun tick(
        level: ServerLevel,
        player: ServerPlayer,
        position: BlockPos
    ) {
    }

    override fun set(player: ServerPlayer) {
    }

    override fun remove(player: ServerPlayer) {
    }
}