package dev.deepslate.fallacy.hydration.hydration

import dev.deepslate.fallacy.common.data.FallacyDataComponents
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.capabilities.ICapabilityProvider

class HydrationProvider : ICapabilityProvider<ItemStack, Void?, IHydration> {
    @Suppress("WRONG_NULLABILITY_FOR_JAVA_OVERRIDE")
    override fun getCapability(
        `object`: ItemStack,
        context: Void?
    ): IHydration? {
        if (`object`.has(FallacyDataComponents.HYDRATION) || `object`.item is ItemHydration) return Hydration(`object`)
        return null
    }
}