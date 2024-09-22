package dev.deepslate.fallacy.race.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import dev.deepslate.fallacy.race.Race
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import kotlin.math.sqrt

class Wood : Race {

    companion object {
        val ID = Fallacy.id("wood")
    }

    override val namespacedId: ResourceLocation = ID

    override val attribute: PlayerAttribute = PlayerAttribute(
        health = 100.0,
        attackDamage = 10.0,
        attackKnockBack = 1.0,
        moveSpeed = 0.091,
        armor = 6.0,
        strength = 5.0,
        burningTime = 2.0,
        scale = sqrt(2.0),
    )

    override fun tick(
        level: ServerLevel,
        player: ServerPlayer,
        position: BlockPos
    ) {
        if (player.isOnFire && player.health > 1.5f) {
            player.health -= Math.clamp(2f, 0f, player.health - 1f)
        }
    }

    private val attackSpeedModifier = AttributeModifier(
        Fallacy.id("wood_race_attack_speed_modifier"),
        -0.75,
        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
    )

    override fun set(player: ServerPlayer) {
        player.getAttribute(Attributes.ATTACK_SPEED)!!.addPermanentModifier(attackSpeedModifier)
    }

    override fun remove(player: ServerPlayer) {
        player.getAttribute(Attributes.ATTACK_SPEED)!!.removeModifier(attackSpeedModifier)
    }
}