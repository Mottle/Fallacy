package dev.deepslate.fallacy.common.loot

import com.mojang.serialization.Codec
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

class ReplaceItem(
    conditions: Array<LootItemCondition>,
    val replaced: ResourceLocation,
    val item: ResourceLocation,
    val multiple: Float
) :
    LootModifier(conditions) {

    companion object {
        val CODEC: MapCodec<ReplaceItem> = RecordCodecBuilder.mapCodec { instance ->
            codecStart(instance).and(
                instance.group(
                    ResourceLocation.CODEC.fieldOf("replaced").forGetter(ReplaceItem::replaced),
                    ResourceLocation.CODEC.fieldOf("item").forGetter(ReplaceItem::item),
                    Codec.FLOAT.optionalFieldOf("multiple", 1.0f).forGetter(ReplaceItem::multiple)
                )
            ).apply(instance, ::ReplaceItem)
        }
    }

    override fun doApply(
        generatedLoot: ObjectArrayList<ItemStack>,
        context: LootContext
    ): ObjectArrayList<ItemStack> = ObjectArrayList(generatedLoot.map {
        if (it.itemHolder.key!!.location() == replaced) {
            val drop = BuiltInRegistries.ITEM.get(item).defaultInstance
            drop.count = (multiple * drop.count).toInt()
            return@map drop
        } else {
            return@map it
        }
    })

    override fun codec(): MapCodec<out IGlobalLootModifier> = CODEC
}