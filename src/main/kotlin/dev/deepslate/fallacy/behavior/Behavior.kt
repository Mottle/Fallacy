package dev.deepslate.fallacy.behavior

import dev.deepslate.fallacy.common.data.BehaviorTags
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer

interface Behavior {
    val tagRequired: List<ResourceLocation>
    fun check(player: ServerPlayer): Boolean {
        val tags = BehaviorTags.get(player)
        return tagRequired.any { tags.contains(it) }
    }
}