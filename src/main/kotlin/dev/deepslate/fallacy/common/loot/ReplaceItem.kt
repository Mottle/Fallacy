package dev.deepslate.fallacy.common.loot

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import it.unimi.dsi.fastutil.objects.ObjectArrayList
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.neoforged.neoforge.common.loot.IGlobalLootModifier
import net.neoforged.neoforge.common.loot.LootModifier

class ReplaceItem(conditions: Array<LootItemCondition>, val replaced: ResourceLocation, val item: ResourceLocation) :
    LootModifier(conditions) {

    companion object {
        val CODEC: MapCodec<ReplaceItem> = RecordCodecBuilder.mapCodec { instance ->
            codecStart(instance).and(
                instance.group(
                    ResourceLocation.CODEC.fieldOf("replaced").forGetter(ReplaceItem::replaced),
                    ResourceLocation.CODEC.fieldOf("item").forGetter(ReplaceItem::item)
                )
            ).apply(instance, ::ReplaceItem)
        }
    }

    override fun doApply(
        generatedLoot: ObjectArrayList<ItemStack>,
        context: LootContext
    ): ObjectArrayList<ItemStack> = ObjectArrayList(generatedLoot.map {
        if (it.itemHolder.key!!.location() == replaced) {
            return@map BuiltInRegistries.ITEM.get(item).defaultInstance
        } else {
            return@map it
        }
    })

    override fun codec(): MapCodec<out IGlobalLootModifier> = CODEC
}