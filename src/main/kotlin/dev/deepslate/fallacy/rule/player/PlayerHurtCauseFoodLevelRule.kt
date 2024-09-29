package dev.deepslate.fallacy.rule.player

import dev.deepslate.fallacy.Fallacy
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.player.Player
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent

//抵消伤害导致的饱食度下降
object PlayerHurtCauseFoodLevelRule {
    fun rule(player: Player, damage: DamageSource) {
        player.foodData.addExhaustion(-damage.foodExhaustion)
    }

    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object Handler {
        @SubscribeEvent
        fun onPostDamage(event: LivingDamageEvent.Post) {
            if (event.entity !is Player) return
            rule(event.entity as Player, event.source)
        }
    }
}