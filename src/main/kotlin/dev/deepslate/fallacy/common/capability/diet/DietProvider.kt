package dev.deepslate.fallacy.common.capability.diet

import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.capabilities.ICapabilityProvider

class DietProvider : ICapabilityProvider<Player, Void?, IDiet> {
    @Suppress("WRONG_NULLABILITY_FOR_JAVA_OVERRIDE")
    override fun getCapability(
        `object`: Player,
        context: Void?
    ): IDiet? = PlayerDiet(`object`)
}