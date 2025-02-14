package dev.deepslate.fallacy.common.capability.heat

import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.capabilities.ICapabilityProvider

class BodyHeatProvider : ICapabilityProvider<Player, Void?, IBodyHeat> {
    @Suppress("WRONG_NULLABILITY_FOR_JAVA_OVERRIDE")
    override fun getCapability(
        `object`: Player,
        context: Void?
    ): IBodyHeat? = PlayerBodyHeat(`object`)
}