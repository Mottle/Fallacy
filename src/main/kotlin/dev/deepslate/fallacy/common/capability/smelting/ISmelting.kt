package dev.deepslate.fallacy.common.capability.smelting

import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidStack

interface ISmelting {
    fun smelt(itemStack: List<ItemStack>): ItemStack

    data class Molten(val containedFluid: MutableList<FluidStack>, val maxCapability: Int) {
        fun peekResult(): FluidStack {
            TODO()
        }

        val amount: Int get() = containedFluid.fold(0) { acc, fluid -> acc + fluid.amount }

        fun fill(fluidStack: FluidStack): FluidStack {
            val amountCanFill = maxCapability - amount

            if (amountCanFill <= 0) return fluidStack
            if (amountCanFill > fluidStack.amount) {
                containedFluid.add(fluidStack.copy())
                return FluidStack.EMPTY
            }

            val added = fluidStack.copy()
            added.amount = amountCanFill
            fluidStack.amount -= amountCanFill
            containedFluid.add(added)

            return fluidStack
        }
    }
}