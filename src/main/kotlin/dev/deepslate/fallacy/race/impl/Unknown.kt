package dev.deepslate.fallacy.race.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import dev.deepslate.fallacy.race.Race
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer

class Unknown : Race {

    companion object {
        val ID = Fallacy.id("unknown")

        //用于缺省，未知种族
        val INSTANCE = Unknown()
    }

    override val namespacedId: ResourceLocation = ID

    override val attribute: PlayerAttribute = PlayerAttribute()

    override fun tick(
        level: ServerLevel,
        player: ServerPlayer,
        position: BlockPos
    ) {
    }

    override fun set(player: ServerPlayer) {
        player.setData(FallacyAttachments.RACE_ID, namespacedId)
    }
}