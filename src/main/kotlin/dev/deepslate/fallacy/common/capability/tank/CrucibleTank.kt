package dev.deepslate.fallacy.common.capability.tank

import dev.deepslate.fallacy.common.block.entity.CrucibleEntity
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import java.lang.ref.WeakReference

class CrucibleTank(private val ref: WeakReference<CrucibleEntity>) : IFluidHandler {
    val entity get() = ref.get()

    override fun getTanks(): Int = 1

    override fun getFluidInTank(tank: Int): FluidStack =
        if (tank != 0) FluidStack.EMPTY else entity?.cachedFluid ?: FluidStack.EMPTY

    override fun getTankCapacity(tank: Int): Int {
        TODO("Not yet implemented")
    }

    override fun isFluidValid(tank: Int, stack: FluidStack): Boolean {
        return false
    }

    override fun fill(
        resource: FluidStack,
        action: IFluidHandler.FluidAction
    ): Int {
        TODO("Not yet implemented")
    }

    override fun drain(
        resource: FluidStack,
        action: IFluidHandler.FluidAction
    ): FluidStack {
        TODO("Not yet implemented")
    }

    override fun drain(
        maxDrain: Int,
        action: IFluidHandler.FluidAction
    ): FluidStack {
        TODO("Not yet implemented")
    }
}