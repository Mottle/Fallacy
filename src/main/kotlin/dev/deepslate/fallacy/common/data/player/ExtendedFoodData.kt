package dev.deepslate.fallacy.common.data.player

import dev.deepslate.fallacy.common.data.FallacyAttributes
import net.minecraft.world.Difficulty
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.food.FoodData
import net.minecraft.world.level.GameRules
import kotlin.math.min

class ExtendedFoodData : FoodData {

    private var tempMaxLevel = 20

//    private var eatenTickTimer = 0

    constructor(player: Player) : super() {
        this.foodLevel = getMaxFoodLevel(player)
        this.lastFoodLevel = foodLevel
    }

    override fun add(foodLevel: Int, saturationLevel: Float) {
        this.foodLevel = (foodLevel + this.foodLevel).coerceIn(0, tempMaxLevel)
        this.saturationLevel =
            (saturationLevel + this.saturationLevel).coerceIn(0f, this.foodLevel.toFloat().coerceAtMost(20f))
    }

    fun getMaxFoodLevel(player: Player): Int {
        tempMaxLevel = player.getAttributeValue(FallacyAttributes.MAX_HUNGER).toInt()
        return tempMaxLevel
    }

    fun reload(player: Player) {
        foodLevel = min(foodLevel, getMaxFoodLevel(player))
    }

    private fun updateExhaustion(difficulty: Difficulty) {
        if (this.exhaustionLevel > 4.0F) {
            this.exhaustionLevel -= 4.0F
            if (this.saturationLevel > 0.0F) {
                this.saturationLevel = (this.saturationLevel - 1.0F).coerceAtLeast(0.0F)
            } else if (difficulty != Difficulty.PEACEFUL) {
                this.foodLevel = (this.foodLevel - 1).coerceAtLeast(0)
            }
        }
    }

    private fun updateLastFoodLevel() {
        this.lastFoodLevel = this.foodLevel
    }

//    private fun updateEatenTickTimer() {
//        if(eatenTickTimer > 0) --eatenTickTimer
//    }

    override fun tick(player: Player) {
        val difficulty = player.level().difficulty
        val maxFoodLevel = getMaxFoodLevel(player)
        val regenFoodLevel = (0.9 * maxFoodLevel).toInt()
        val isNaturalRegen = player.level().gameRules.getBoolean(GameRules.RULE_NATURAL_REGENERATION)
        val scale = player.getAttributeValue(Attributes.SCALE).toFloat().coerceAtMost(0.5f)

        updateLastFoodLevel()
        updateExhaustion(difficulty)
//        updateEatenTickTimer()

        if (isNaturalRegen && this.saturationLevel > 0.0F && player.isHurt && this.foodLevel >= maxFoodLevel) {
            this.tickTimer++
            if (this.tickTimer >= 10) {
                val f = this.saturationLevel.coerceAtMost(6.0F) * scale
                player.heal(f / 6.0F)
                this.addExhaustion(f)
                this.tickTimer = 0
            }
        } else if (isNaturalRegen && this.foodLevel >= regenFoodLevel && player.isHurt) {
            this.tickTimer++
            if (this.tickTimer >= 80) {
                player.heal(1.0F * scale)
                this.addExhaustion(6.0F * scale)
                this.tickTimer = 0
            }
        } else if (this.foodLevel <= 0) {
            this.tickTimer++
            if (this.tickTimer >= 80) {
                if (player.health > 10.0F || difficulty == Difficulty.HARD || player.health > 1.0F && difficulty == Difficulty.NORMAL) {
                    val damage = (player.getAttributeValue(Attributes.MAX_HEALTH).toFloat() / 20f).coerceAtMost(1f)
                    player.hurt(player.damageSources().starve(), damage)
                }
                this.tickTimer = 0
            }
        } else {
            this.tickTimer = 0
        }
    }

    override fun needsFood(): Boolean = foodLevel < tempMaxLevel
}