package dev.deepslate.fallacy.client.tooltip.component

import net.minecraft.core.Holder
import net.minecraft.world.inventory.tooltip.TooltipComponent
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class ItemTooltipComponent: TooltipComponent {
    val item: Item

    var preString: String? = null
        private set

    var postString: String? = null
        private set

    constructor(item: Item) {
        this.item = item
    }

    constructor(stack: ItemStack) {
        this.item = stack.item
    }

    constructor(holder: Holder<Item>) {
        this.item = holder.value()
    }

    fun withPre(string: String) = apply { preString = string }

    fun withPost(string: String) = apply { postString = string }
}