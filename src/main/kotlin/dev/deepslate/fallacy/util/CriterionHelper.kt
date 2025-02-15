package dev.deepslate.fallacy.util

import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.advancements.critereon.ItemPredicate
import net.minecraft.world.level.ItemLike
import java.util.*

object CriterionHelper {
    fun inventoryTrigger(vararg predicates: ItemPredicate): Criterion<InventoryChangeTrigger.TriggerInstance> =
        CriteriaTriggers.INVENTORY_CHANGED
            .createCriterion(
                InventoryChangeTrigger.TriggerInstance(
                    Optional.empty(),
                    InventoryChangeTrigger.TriggerInstance.Slots.ANY,
                    listOf<ItemPredicate>(*predicates)
                )
            )

    fun inventoryTrigger(vararg items: ItemPredicate.Builder): Criterion<InventoryChangeTrigger.TriggerInstance> =
        inventoryTrigger(*(items.map(ItemPredicate.Builder::build).toTypedArray()))

    fun has(itemLike: ItemLike): Criterion<InventoryChangeTrigger.TriggerInstance> =
        inventoryTrigger(ItemPredicate.Builder.item().of(itemLike))
}