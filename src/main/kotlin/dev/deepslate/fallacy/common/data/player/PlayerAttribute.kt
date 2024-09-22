package dev.deepslate.fallacy.common.data.player

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.FallacyAttributes
import net.minecraft.core.Holder
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.Attributes

data class PlayerAttribute(
    val armor: Double = 0.0,//护甲值[0, 30] -> [0. 1024]
    val toughness: Double = 0.0,//韧性[0, 20] -> [0, 256]
    val attackDamage: Double = 1.0,//攻击伤害[0, 2048]
    val attackKnockBack: Double = 0.0,//击退[0, 5]
    val attackSpeed: Double = 4.0,//攻击速度[0, 1024] 不要改！！！
    val burningTime: Double = 1.0,//着火时间[0, 1024] (假设生物本应被着火t游戏刻，此属性计算值为r，则生物的剩余着火时间将不会低t * r)
    val explosionKnockBackResistance: Double = 0.0,//爆炸击退抗性[0, 1]
    val fallDamageMultiplier: Double = 1.0,//摔落伤害系数[0, 100]
    val gravity: Double = 0.08,//重力[-1, 1]
    val jumpStrength: Double = 0.41999998688697815,//跳跃力度[0, 32]
    val knockBackResistance: Double = 0.0,//击退抗性[0, 1]
    val luck: Double = 0.0,//幸运[-1024, 1024]
    val health: Double = 20.0,//生命值[1, 1024] -> [1, 32768]
    val moveEfficiency: Double = 0.0,//移动效率[0, 1] (影响生物在方块（灵魂沙、蜂蜜块等）上移动的属性)
    val moveSpeed: Double = 0.10000000149011612,//移动速度[0, 1024]
    val oxygenBonus: Double = 0.0,//额外氧气[0, 1024] (游戏尝试降低氧气值一定成功；而此属性计算值e不为0时，游戏尝试降低氧气值只有1⁄e+1的概率成功)
    val safeFallDistance: Double = 3.0,//安全摔落高度[-1024, 1024]
    val scale: Double = 1.0,//尺寸[0.0625, 16]
    val stepHeight: Double = 0.6,//最大行走高度[0, 10] (玩家潜行时不会走下方块的最小高度差)
    val waterMoveEfficiency: Double = 0.0,//水移动效率[0, 1] (当此属性计算值为1时，在水中行走和在空气中行走的效果完全一致)
    val blockBreakSpeed: Double = 1.0,//方块破坏速度[0, 1024]
    val blockInteractionRange: Double = 4.5,//方块交互范围[0, 64]
    val entityInteractionRange: Double = 3.0,//实体交互范围[0, 64]
    val miningEfficiency: Double = 0.0,//挖掘效率[0, 1024]
    val sneakingSpeed: Double = 0.3,//潜行速度[0, 1]
    val submergedMiningSpeed: Double = 0.2,//水下挖掘速度[0, 20]
    val sweepingDamageRatio: Double = 0.0,//横扫伤害系数[0, 1] (横扫攻击伤害的最终值为1 + r * d，其中r为横扫伤害比率，d为原近战攻击伤害)

    val hunger: Double = 20.0,
    val thirst: Double = 20.0,
    val strength: Double = 1.0,
    val magicResistance: Double = 0.0,
    val magicStrength: Double = 0.0
) {
    fun set(player: ServerPlayer, refresh: Boolean = true) {
        set(player, Attributes.ARMOR, armor)
        set(player, Attributes.ARMOR_TOUGHNESS, toughness)
        set(player, Attributes.ATTACK_DAMAGE, attackDamage)
        set(player, Attributes.ATTACK_KNOCKBACK, attackKnockBack)
        set(player, Attributes.ATTACK_SPEED, attackSpeed)
        set(player, Attributes.BURNING_TIME, burningTime)
        set(player, Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, explosionKnockBackResistance)
        set(player, Attributes.FALL_DAMAGE_MULTIPLIER, fallDamageMultiplier)
        set(player, Attributes.GRAVITY, gravity)
        set(player, Attributes.JUMP_STRENGTH, jumpStrength)
        set(player, Attributes.KNOCKBACK_RESISTANCE, knockBackResistance)
        set(player, Attributes.LUCK, luck)
        set(player, Attributes.MAX_HEALTH, health)
        set(player, Attributes.MOVEMENT_EFFICIENCY, moveEfficiency)
        set(player, Attributes.MOVEMENT_SPEED, moveSpeed)
        set(player, Attributes.OXYGEN_BONUS, oxygenBonus)
        set(player, Attributes.SAFE_FALL_DISTANCE, safeFallDistance)
        set(player, Attributes.SCALE, scale)
        set(player, Attributes.STEP_HEIGHT, stepHeight)
        set(player, Attributes.WATER_MOVEMENT_EFFICIENCY, waterMoveEfficiency)
        set(player, Attributes.BLOCK_BREAK_SPEED, blockBreakSpeed)
        set(player, Attributes.BLOCK_INTERACTION_RANGE, blockInteractionRange)
        set(player, Attributes.ENTITY_INTERACTION_RANGE, entityInteractionRange)
        set(player, Attributes.MINING_EFFICIENCY, miningEfficiency)
        set(player, Attributes.SNEAKING_SPEED, sneakingSpeed)
        set(player, Attributes.SUBMERGED_MINING_SPEED, submergedMiningSpeed)
        set(player, Attributes.SWEEPING_DAMAGE_RATIO, sweepingDamageRatio)
        //fallacy
        set(player, FallacyAttributes.MAX_HUNGER, hunger)
        set(player, FallacyAttributes.MAX_THIRST, thirst)
        set(player, FallacyAttributes.STRENGTH, strength)
        set(player, FallacyAttributes.MAGIC_RESISTANCE, magicResistance)
        set(player, FallacyAttributes.MAGIC_STRENGTH, magicStrength)

        if (refresh) {
            player.foodData.foodLevel = hunger.toInt()
            player.health = health.toFloat()
        }
    }

    private fun set(player: ServerPlayer, holder: Holder<Attribute>, value: Double) {
        val instance = player.getAttribute(holder)
        if (instance == null) {
            Fallacy.LOGGER.warn("Attribute ${holder.value().descriptionId} is not found on player ${player.name}.")
            return
        }

        instance.baseValue = value
    }
}