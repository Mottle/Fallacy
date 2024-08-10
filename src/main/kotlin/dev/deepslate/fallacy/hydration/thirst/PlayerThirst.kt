package dev.deepslate.fallacy.hydration.thirst

import dev.deepslate.fallacy.common.capability.FallacyAttachments
import net.minecraft.world.entity.player.Player
import kotlin.math.min

class PlayerThirst(val player: Player): IThirst {
    override var thirst: Float
        get() = player.getData(FallacyAttachments.THIRST)
        set(value) { player.setData(FallacyAttachments.THIRST, value) }
    override val max: Float = 20f

    override fun drink(value: Float) {
        thirst = min(max, thirst + value)
    }
}