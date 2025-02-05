package dev.deepslate.fallacy.behavior

import com.mojang.serialization.Codec
import net.minecraft.resources.ResourceLocation

data class BehaviorContainer(private val behaviorSet: Set<Behavior>) : Iterable<Behavior> {
    companion object {
        fun empty() = BehaviorContainer(setOf())

        fun from(behaviors: Collection<Behavior>) = BehaviorContainer(behaviors.toSet())

        private fun fromResourceLocations(keys: Collection<ResourceLocation>) =
            from(keys.mapNotNull(::fromResourceLocation))

        private fun fromResourceLocation(key: ResourceLocation): Behavior? = Behaviors.REGISTRY.get(key)

        val CODEC: Codec<BehaviorContainer> =
            ResourceLocation.CODEC.listOf().xmap(::fromResourceLocations, BehaviorContainer::toResourceLocations)
    }

    fun toResourceLocations(): List<ResourceLocation> = behaviorSet.mapNotNull(Behaviors.REGISTRY::getKey)

    fun has(tag: ResourceLocation): Boolean = Behaviors.REGISTRY.get(tag)?.let { it in behaviorSet } == true

    fun has(behavior: Behavior): Boolean = behavior in behaviorSet

    fun add(behavior: Behavior): BehaviorContainer = BehaviorContainer(behaviorSet + behavior)

    fun remove(behavior: Behavior): BehaviorContainer = BehaviorContainer(behaviorSet - behavior)

    fun addAll(behaviors: Collection<Behavior>): BehaviorContainer = BehaviorContainer(behaviorSet + behaviors)

    fun removeAll(behaviors: Collection<Behavior>): BehaviorContainer = BehaviorContainer(behaviorSet - behaviors)

    override fun iterator(): Iterator<Behavior> = behaviorSet.iterator()
}