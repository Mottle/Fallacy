package dev.deepslate.fallacy.common.capability.skeleton

import dev.deepslate.fallacy.race.Race
import dev.deepslate.fallacy.race.impl.Skeleton
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.capabilities.ICapabilityProvider

class SkeletonProvider : ICapabilityProvider<Player, Void?, ISkeleton> {
    @Suppress("WRONG_NULLABILITY_FOR_JAVA_OVERRIDE")
    override fun getCapability(
        `object`: Player,
        context: Void?
    ): ISkeleton? {
        val race = Race.get(`object`)
        if (race is Skeleton) return PlayerSkeleton(`object`)
        return null
    }
}