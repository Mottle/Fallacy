package dev.deepslate.fallacy.common.data.player

data class PlayerAttribute(
    val armor: Float = 0f,//护甲值[0, 30]
    val toughness: Float = 0f,//韧性[0, 20]
    val attackDamage: Float = 1f,//攻击伤害[0, 2048]
    val attachKnockBack: Float = 0f,//击退[0, 5]
    val attackSpeed: Float = 4f,//攻击速度[0, 1024]
    val burningTime: Float = 1f,//着火时间[0, 1024] (假设生物本应被着火t游戏刻，此属性计算值为r，则生物的剩余着火时间将不会低t * r)
    val explosionKnockBackResistance: Float = 0f,//爆炸击退抗性[0, 1]
    val fallDamageMultiplier: Float = 1f,//摔落伤害系数[0, 100]
    val gravity: Float = 0.08f,//重力[-1, 1]
    val jumpStrength: Float = 0.42f,//跳跃力度[0, 32]
    val knockBackResistance: Float = 0f,//击退抗性[0, 1]
    val luck: Float = 0f,//幸运[-1024, 1024]
    val health: Float = 20f,//生命值[1, 1024]
    val moveEfficiency: Float = 0f,//移动效率[0, 1] (影响生物在方块（灵魂沙、蜂蜜块等）上移动的属性)
    val moveSpeed: Float = 0.1f,//移动速度[0, 1024]
    val oxygenBonus: Float = 0f,//额外氧气[0, 1024] (游戏尝试降低氧气值一定成功；而此属性计算值e不为0时，游戏尝试降低氧气值只有1⁄e+1的概率成功)
    val safeFallDistance: Float = 3f,//安全摔落高度[-1024, 1024]
    val scale: Float = 1f,//尺寸[0.0625, 16]
    val stepHeight: Float = 0.6f,//最大行走高度[0, 10] (玩家潜行时不会走下方块的最小高度差)
    val waterMoveEfficiency: Float = 0f,//水移动效率[0, 1] (当此属性计算值为1时，在水中行走和在空气中行走的效果完全一致)
    val blockBreakSpeed: Float = 1f,//方块破坏速度[0, 1024]
    val blockInteractionRange: Float = 4.5f,//方块交互范围[0, 64]
    val entityInteractionRange: Float = 3f,//实体交互范围[0, 64]
    val miningEfficiency: Float = 0f,//挖掘效率[0, 1024]
    val sneakingSpeed: Float = 0.3f,//潜行速度[0, 1]
    val submergedMiningSpeed: Float = 0.2f,//水下挖掘速度[0, 20]
    val sweepingDamageRatio: Float = 0f,//横扫伤害系数[0, 1] (横扫攻击伤害的最终值为1+rd，其中r为横扫伤害比率，d为原近战攻击伤害)
)