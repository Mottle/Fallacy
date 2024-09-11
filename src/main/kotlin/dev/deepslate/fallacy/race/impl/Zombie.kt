package dev.deepslate.fallacy.race.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import dev.deepslate.fallacy.race.Race
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ItemStack
import kotlin.math.max

class Zombie : Race {

    companion object {
        val ID = Fallacy.id("zombie")

        private val WEAKNESS2 = MobEffectInstance(MobEffects.WEAKNESS, 20 * 4, 1)

        private val WEAKNESS = MobEffectInstance(MobEffects.WEAKNESS, 20, 0)
    }

    override val namespacedId: ResourceLocation = ID

    override val attribute: PlayerAttribute = PlayerAttribute(health = 40.0, attackDamage = 4.0)

    override fun tick(
        level: ServerLevel,
        player: ServerPlayer,
        position: BlockPos
    ) {
        if (player.isInvulnerable) return
        if (!level.isDay) return
        if (!level.canSeeSky(position)) return
        with(player) {
            if (isInWaterRainOrBubble) return
            if (isInPowderSnow) return
            if (wasInPowderSnow) return
        }

        val head = player.getItemBySlot(EquipmentSlot.HEAD)
        if (!head.isEmpty) {
            if (head.isDamageableItem) {
                val damage = max(head.maxDamage / 120, 1)

                head.damageValue += player.random.nextInt(damage)
                if (head.damageValue >= head.maxDamage) {
                    player.onEquippedItemBroken(head.item, EquipmentSlot.HEAD)
                    player.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY)
                }
            }
            player.addEffect(WEAKNESS)
        } else {
            player.igniteForTicks(20 * 4)
            player.addEffect(WEAKNESS2)
        }
    }

    override fun set(player: ServerPlayer) {
        attribute.set(player)
    }
}