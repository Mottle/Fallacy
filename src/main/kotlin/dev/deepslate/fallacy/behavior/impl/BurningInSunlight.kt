package dev.deepslate.fallacy.behavior.impl

import dev.deepslate.fallacy.behavior.BehaviorTags
import dev.deepslate.fallacy.behavior.TickableBehavior
import dev.deepslate.fallacy.util.EntityHelper
import dev.deepslate.fallacy.util.announce.Autoload
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ItemStack
import kotlin.math.max

@Autoload
class BurningInSunlight : TickableBehavior {
    override val interval: Int = 20 * 2

    override val tagRequired: List<ResourceLocation> = listOf(BehaviorTags.BURNING_IN_SUNLIGHT)

    override fun tick(
        level: ServerLevel,
        player: ServerPlayer,
        position: BlockPos
    ) {
        if (!EntityHelper.checkUndeadBurning(level, player, position)) return

        val head = player.getItemBySlot(EquipmentSlot.HEAD)
        if (!head.isEmpty) {
            if (head.isDamageableItem) damageHeadByRandom(head, player)
        } else {
            player.igniteForTicks(20 * 4)
        }
    }

    private fun damageHeadByRandom(head: ItemStack, player: ServerPlayer) {
        val damage = max(head.maxDamage / 120, 1)
        head.damageValue += player.random.nextInt(damage)
        if (head.damageValue >= head.maxDamage) {
            player.onEquippedItemBroken(head.item, EquipmentSlot.HEAD)
            player.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY)
        }
    }
}