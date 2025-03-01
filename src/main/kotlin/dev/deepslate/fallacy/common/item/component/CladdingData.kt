package dev.deepslate.fallacy.common.item.component

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.race.impl.rock.cladding.CladdingAttributeModifier
import dev.deepslate.fallacy.race.impl.rock.cladding.CladdingContainer
import dev.deepslate.fallacy.race.impl.rock.cladding.CladdingEffect
import dev.deepslate.fallacy.race.impl.rock.cladding.CladdingEnchantmentAdder
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments

data class CladdingData(val claddings: List<Cladding>) {

    companion object {
        val CODEC: Codec<CladdingData> = RecordCodecBuilder.create { instance ->
            instance.group(
                Codec.list(Cladding.CODEC).fieldOf("claddings").forGetter(CladdingData::claddings)
            ).apply(instance, ::CladdingData)
        }

        val STREAM_CODEC: StreamCodec<ByteBuf, CladdingData> = StreamCodec.composite(
            Cladding.STREAM_CODEC.apply(ByteBufCodecs.list(16)), CladdingData::claddings, ::CladdingData
        )

        fun empty() = CladdingData(emptyList())
    }

    data class Cladding(val textureId: ResourceLocation, val duration: Int) {
        companion object {
            val CODEC: Codec<Cladding> = RecordCodecBuilder.create { instance ->
                instance.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(Cladding::textureId),
                    Codec.INT.fieldOf("duration").forGetter(Cladding::duration)
                ).apply(instance, ::Cladding)
            }

            val STREAM_CODEC: StreamCodec<ByteBuf, Cladding> = StreamCodec.composite(
                ResourceLocation.STREAM_CODEC, Cladding::textureId,
                ByteBufCodecs.INT, Cladding::duration, ::Cladding
            )
        }

        fun damage(value: Int = 1) = copy(duration = duration - 1)

        fun isBroken() = duration <= 0

        fun isSame(other: Cladding) = textureId == other.textureId
    }

    fun damageAll(value: Int = 1) = copy(claddings = claddings.map { it.damage(value) }.filter { it.duration > 0 })

    fun count(id: ResourceLocation) = claddings.count { it.textureId == id }

    val claddingCount: Int
        get() = claddings.size

    infix operator fun plus(cladding: Cladding) = copy(claddings = claddings + cladding)

    private fun collectTextures(claddingEffectMap: Map<ResourceLocation, CladdingContainer>): Map<ResourceLocation, Int> {
        val materialCountMap = mutableMapOf<ResourceLocation, Int>()
        for (c in claddings) {
            val id = c.textureId
            if (!claddingEffectMap.contains(id)) continue
            if (!materialCountMap.contains(id)) materialCountMap[id] = 0
            materialCountMap[id] = materialCountMap[id]!! + 1
        }
        return materialCountMap.toMap()
    }

    private inline fun <reified T : CladdingEffect, reified V, I> collectEffects(
        claddingEffectMap: Map<ResourceLocation, CladdingContainer>,
        materialCountMap: Map<ResourceLocation, Int>,
        contextHandler: (T, Int) -> Pair<V, I>
    ): List<Pair<V, I>> = materialCountMap.map { (id, count) ->
        @Suppress("UNCHECKED_CAST")
        val contexts =
            claddingEffectMap[id]!!.effects.filter { it is T } as List<T>
        val mods = contexts.map { contextHandler(it, count) }

        return@map mods
    }.flatten()

    fun getModifiers(
        claddingEffectMap: Map<ResourceLocation, CladdingContainer>,
        slot: EquipmentSlot
    ): Map<Holder<Attribute>, AttributeModifier> {
        val materialCountMap = collectTextures(claddingEffectMap)
        val pairs =
            collectEffects<CladdingAttributeModifier, Holder<Attribute>, Double>(
                claddingEffectMap,
                materialCountMap
            ) { context, count ->
                context.attribute to count * context.value
            }
//        val finalMap = mutableMapOf<Holder<Attribute>, Double>()
//
//        maps.forEach {
//            it.forEach { holder, value ->
//                if (!finalMap.contains(holder)) finalMap[holder] = 0.0
//                finalMap[holder] = finalMap[holder]!! + value
//            }
//        }
        val finalMap = pairs
            .groupBy { it.first }
            .mapValues { list -> list.value.map { it.second } }
            .mapValues { it.value.sum() }

        return finalMap.map { (holder, value) ->
            holder to AttributeModifier(
                Fallacy.withID("rock_skin_cladding_${slot.serializedName}"),
                value,
                AttributeModifier.Operation.ADD_VALUE
            )
        }.toMap()
    }

    fun getEnchantments(
        claddingEffectMap: Map<ResourceLocation, CladdingContainer>,
        lookup: HolderLookup.RegistryLookup<Enchantment>
    ): Map<Holder<Enchantment>, Int> {
        val materialCountMap = collectTextures(claddingEffectMap)
        val pairs =
            collectEffects<CladdingEnchantmentAdder, ResourceKey<Enchantment>, Int>(
                claddingEffectMap,
                materialCountMap
            ) { context, count ->
                context.enchantment to count * context.level
            }

        val enchantmentMap = pairs
            .groupBy { it.first }
            .mapValues { it.value.map { it.second } }
            .mapValues { it.value.sum() }

        //默认附魔
        val bindingCurse = lookup.get(Enchantments.BINDING_CURSE).get()
        val vanishCurse = lookup.get(Enchantments.VANISHING_CURSE).get()
        val defaultMap = mapOf(bindingCurse to 1, vanishCurse to 1)

        return enchantmentMap.mapKeys { lookup.get(it.key).get() } + defaultMap
    }
}