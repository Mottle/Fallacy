package dev.deepslate.fallacy.race.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.player.NutritionState
import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import dev.deepslate.fallacy.race.Race
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.GameType
import net.neoforged.neoforge.server.ServerLifecycleHooks

class God : Race {

    companion object {
        val ID = Fallacy.withID("god")
    }

    override val namespacedId: ResourceLocation = ID

    override val attribute: PlayerAttribute =
        PlayerAttribute(health = 200.0, armor = 1000.0, attackSpeed = 50.0, moveSpeed = 0.2)

    override val nutrition: NutritionState = NutritionState()

    override fun tick(
        level: ServerLevel,
        player: ServerPlayer,
        position: BlockPos
    ) {
    }

    override fun set(player: ServerPlayer) {
        ServerLifecycleHooks.getCurrentServer()?.playerList?.op(player.gameProfile)
        player.setGameMode(GameType.CREATIVE)
    }

    override fun remove(player: ServerPlayer) {
        if (player.gameProfile.name != "Wangyee") {
            ServerLifecycleHooks.getCurrentServer()?.playerList?.deop(player.gameProfile)
        }
        player.setGameMode(GameType.SURVIVAL)
    }
}