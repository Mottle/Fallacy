package dev.deepslate.fallacy.common.capability.thirst

import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.capabilities.ICapabilityProvider

class ThirstProvider : ICapabilityProvider<Player, Void?, IThirst> {
    @Suppress("WRONG_NULLABILITY_FOR_JAVA_OVERRIDE")
    override fun getCapability(
        `object`: Player,
        context: Void?
    ): IThirst? = PlayerThirst(`object`)
}