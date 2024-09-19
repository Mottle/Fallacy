package dev.deepslate.fallacy.behavior

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.FallacyAttachments
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer

object BehaviorTags {
    val UNDEAD = Fallacy.id("undead")

    val BURNING_IN_SUNLIGHT = Fallacy.id("burning_in_sunlight")

    val WEAKNESS2_IN_SUNLIGHT = Fallacy.id("weakness2_in_sunlight")

    val WEAKNESS_IN_DAY = Fallacy.id("weakness_in_day")

    fun get(player: ServerPlayer) = player.getData(FallacyAttachments.BEHAVIOR_TAGS)

    fun has(player: ServerPlayer, behaviorTag: ResourceLocation) = behaviorTag in get(player)

    fun set(player: ServerPlayer, vararg behaviorTags: ResourceLocation) {
        val new = get(player).toSet() + behaviorTags
        player.setData(FallacyAttachments.BEHAVIOR_TAGS, new.toList())
    }

    fun remove(player: ServerPlayer, vararg behaviorTags: ResourceLocation) {
        val new = get(player).toSet() - behaviorTags
        player.setData(FallacyAttachments.BEHAVIOR_TAGS, new.toList())
    }
}