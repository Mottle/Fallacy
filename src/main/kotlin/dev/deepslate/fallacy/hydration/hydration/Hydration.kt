package dev.deepslate.fallacy.hydration.hydration

import dev.deepslate.fallacy.common.data.FallacyDataComponents
import net.minecraft.world.item.ItemStack

class Hydration(val stack: ItemStack) : IHydration {
    override var hydration: Float
        get() {
            if(stack.has(FallacyDataComponents.HYDRATION)) return stack.get(FallacyDataComponents.HYDRATION)!!
            return (stack.item as ItemHydration).hydration
        }
        set(value) {
            stack.set(FallacyDataComponents.HYDRATION, value)
        }
}