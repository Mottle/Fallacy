package dev.deepslate.fallacy.common.capability.inventory

import dev.deepslate.fallacy.common.block.entity.CrucibleEntity
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler
import java.lang.ref.WeakReference

class CrucibleInventory(private val ref: WeakReference<CrucibleEntity>) : IItemHandler {
    val entity get() = ref.get()

    override fun getSlots(): Int = CrucibleEntity.INVENTORY_SIZE

    override fun getStackInSlot(slot: Int): ItemStack {
        if (slot >= getSlots() || slot < 0) return ItemStack.EMPTY
        return entity?.inventory[slot] ?: ItemStack.EMPTY
    }

    override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
        if (!isItemValid(slot, stack)) return stack

        val copied = stack.copy()
        copied.count = 1
        entity!!.inventory[slot] = copied

        if (stack.count <= 1) return ItemStack.EMPTY

        stack.count -= 1
        return stack
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
        if (slot >= CrucibleEntity.INVENTORY_SIZE) return ItemStack.EMPTY
        if (!entity!!.inventory[slot].isEmpty) {
            val ret = entity!!.inventory[slot]
            entity!!.inventory[slot] = ItemStack.EMPTY
            return ret
        }

        return ItemStack.EMPTY
    }

    override fun getSlotLimit(slot: Int): Int = 1

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
        if (entity == null) return false
        if (slot >= CrucibleEntity.INVENTORY_SIZE) return false
//        if (!stack.tags.anyMatch { tag -> FallacyItemTags.SMELTABLE == tag }) return false

        return entity?.inventory[slot]?.isEmpty != false
    }
}