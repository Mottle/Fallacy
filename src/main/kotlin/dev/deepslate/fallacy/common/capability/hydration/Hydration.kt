package dev.deepslate.fallacy.common.capability.hydration

import dev.deepslate.fallacy.common.item.component.FallacyDataComponents
import dev.deepslate.fallacy.common.item.ItemHydration
import net.minecraft.world.item.ItemStack

class Hydration(val stack: ItemStack) : IHydration {
    override var hydration: Float
        get() {
            if (stack.has(FallacyDataComponents.HYDRATION)) return stack.get(FallacyDataComponents.HYDRATION)!!
            return (stack.item as ItemHydration).hydration
        }
        set(value) {
            stack.set(FallacyDataComponents.HYDRATION, value)
        }
}