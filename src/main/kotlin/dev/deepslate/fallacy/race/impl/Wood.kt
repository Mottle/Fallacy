package dev.deepslate.fallacy.race.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.player.NutritionState
import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import dev.deepslate.fallacy.race.Race
import dev.deepslate.fallacy.race.Respawnable
import dev.deepslate.fallacy.race.resources.AttributeResource
import dev.deepslate.fallacy.race.resources.NutritionResource
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import kotlin.math.pow

class Wood : Race(), Respawnable {

    companion object {
        val ID = Fallacy.withID("wood")
    }

    override val namespacedId: ResourceLocation = ID

//    override val attribute: PlayerAttribute = PlayerAttribute(
//        health = 100.0,
//        attackDamage = 10.0,
//        attackKnockBack = 1.0,
//        moveSpeed = 9.1 / 100.0,
//        armor = 6.0,
//        strength = 5.0,
//        gravity = 0.08 * 1.5,
//        jumpStrength = 0.42 * 1.195, // Magic number
//        fallDamageMultiplier = 0.95,
//        burningTime = 2.0,
//        scale = 2.0.pow(1.0 / 3.0),
//        entityInteractionRange = 3.0 * 2.0.pow(1.0 / 3.0),
//        blockInteractionRange = 4.5 * 2.0.pow(1.0 / 3.0),
//        knockBackResistance = 0.3,
//        hunger = 80.0
//    )
//
//    override val nutrition: NutritionState =
//        NutritionState(fat = NutritionState.Nutrition.NoNeed, protein = NutritionState.Nutrition.NoNeed)

    override val resources: Map<String, Resource> = mapOf(
        AttributeResource.KEY to AttributeResource(
            PlayerAttribute(
                health = 100.0,
                attackDamage = 10.0,
                attackKnockBack = 1.0,
                moveSpeed = 9.1 / 100.0,
                armor = 6.0,
                strength = 5.0,
                gravity = 0.08 * 1.5,
                jumpStrength = 0.42 * 1.195, // Magic number
                fallDamageMultiplier = 0.95,
                burningTime = 2.0,
                scale = 2.0.pow(1.0 / 3.0),
                entityInteractionRange = 3.0 * 2.0.pow(1.0 / 3.0),
                blockInteractionRange = 4.5 * 2.0.pow(1.0 / 3.0),
                knockBackResistance = 0.3,
                hunger = 80.0
            )
        ),
        NutritionResource.KEY to NutritionResource(
            NutritionState(
                fat = NutritionState.Nutrition.NoNeed,
                protein = NutritionState.Nutrition.NoNeed
            )
        )
    )

    override fun tick(
        level: ServerLevel,
        player: ServerPlayer,
        position: BlockPos
    ) {
        if (player.isInvulnerable) return
        if (player.isOnFire && player.health > 1.5f) {
            player.health -= Math.clamp(2f, 0f, player.health - 1f)
        }
    }

    private val attackSpeedModifier = AttributeModifier(
        Fallacy.withID("wood_race_attack_speed_modifier"),
        -0.75,
        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
    )

    override fun apply(player: ServerPlayer) {
        player.getAttribute(Attributes.ATTACK_SPEED)!!.addPermanentModifier(attackSpeedModifier)
    }

    override fun deapply(player: ServerPlayer) {
        player.getAttribute(Attributes.ATTACK_SPEED)!!.removeModifier(attackSpeedModifier)
    }

    override fun onRespawn(
        player: ServerPlayer
    ) {
        player.getAttribute(Attributes.ATTACK_SPEED)!!.addPermanentModifier(attackSpeedModifier)
    }
}