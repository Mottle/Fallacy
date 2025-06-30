package dev.deepslate.fallacy.trait

import com.mojang.serialization.Codec
import net.minecraft.resources.ResourceLocation

data class BehaviorContainer(private val traitSet: Set<Trait>) : Iterable<Trait> {
    companion object {
        fun empty() = BehaviorContainer(setOf())

        fun from(traits: Collection<Trait>) = BehaviorContainer(traits.toSet())

        private fun fromResourceLocations(keys: Collection<ResourceLocation>) =
            from(keys.mapNotNull(::fromResourceLocation))

        private fun fromResourceLocation(key: ResourceLocation): Trait? = Traits.REGISTRY.get(key)

        val CODEC: Codec<BehaviorContainer> =
            ResourceLocation.CODEC.listOf().xmap(::fromResourceLocations, BehaviorContainer::toResourceLocations)
    }

    fun toResourceLocations(): List<ResourceLocation> = traitSet.mapNotNull(Traits.REGISTRY::getKey)

    fun has(tag: ResourceLocation): Boolean = Traits.REGISTRY.get(tag)?.let { it in traitSet } == true

    fun has(trait: Trait): Boolean = trait in traitSet

    fun add(trait: Trait): BehaviorContainer = BehaviorContainer(traitSet + trait)

    fun remove(trait: Trait): BehaviorContainer = BehaviorContainer(traitSet - trait)

    fun addAll(traits: Collection<Trait>): BehaviorContainer = BehaviorContainer(traitSet + traits)

    fun removeAll(traits: Collection<Trait>): BehaviorContainer = BehaviorContainer(traitSet - traits)

    override fun iterator(): Iterator<Trait> = traitSet.iterator()
}