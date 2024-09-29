package dev.deepslate.fallacy.hydration

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.capability.FallacyCapabilities
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.common.network.packet.DrinkInWorldPacket
import dev.deepslate.fallacy.common.network.packet.ThirstSyncPacket
import dev.deepslate.fallacy.util.TickHelper
import net.minecraft.advancements.CriteriaTriggers
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.level.block.AnvilBlock
import net.minecraft.world.level.block.BarrelBlock
import net.neoforged.bus.api.EventPriority
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.util.TriState
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent
import net.neoforged.neoforge.event.tick.PlayerTickEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext

@EventBusSubscriber(modid = Fallacy.MOD_ID)
object Handler {
    const val BAST_TICK_RATE = 20

    @SubscribeEvent
    fun onPlayerTick(event: PlayerTickEvent.Post) {
        if (!TickHelper.checkServerTickRate(BAST_TICK_RATE)) return

        val player = event.entity as? ServerPlayer ?: return
        val thirst = player.getCapability(FallacyCapabilities.THIRST)!!

        thirst.tick()
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    fun onPlayerRightClickBlock(event: PlayerInteractEvent.RightClickBlock) {
        val level = event.level
        val state = level.getBlockState(event.pos)
        val stack = event.itemStack
        val hand = event.hand
        val player = event.entity

        if (!player.isShiftKeyDown) return
        if (stack.isEmpty && !event.isCanceled && hand == InteractionHand.MAIN_HAND) {
            val useBlockResult = state.useItemOn(stack, level, player, hand, event.hitVec)
            if (useBlockResult.consumesAction()) {
                if (player is ServerPlayer) {
                    CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(player, event.pos, stack)
                }
                event.isCanceled = true
                event.cancellationResult = useBlockResult.result()
            }
        } else {
            val result = Drink.attemptDrink(level, player)
            if (result != InteractionResult.PASS) {
                event.isCanceled = true
                event.cancellationResult = result
            }
        }

        if (state.block is AnvilBlock || state.block is BarrelBlock) event.useBlock = TriState.TRUE
    }

    //只在Client Side触发，遂向Server发送DrinkInWorldPacket
    @SubscribeEvent
    fun onPlayerRightClickEmpty(event: PlayerInteractEvent.RightClickEmpty) {
        val player = event.entity

        if (event.hand != InteractionHand.MAIN_HAND || !event.itemStack.isEmpty) return
        if (!player.isShiftKeyDown) return

        val result = Drink.attemptDrink(event.level, player)

        if (result != InteractionResult.SUCCESS) return
        PacketDistributor.sendToServer(DrinkInWorldPacket.PACKET)
    }

    //从末地归来复制数据
    @SubscribeEvent
    fun onPlayerDataClone(event: PlayerEvent.Clone) {
        val origin = event.original
        if (!event.isWasDeath) {
            val player = event.entity
            val oldThirst = origin.getData(FallacyAttachments.THIRST)
            val oldDrinkTicks = origin.getData(FallacyAttachments.LAST_DRINK_TICK)

            player.setData(FallacyAttachments.THIRST, oldThirst)
            player.setData(FallacyAttachments.LAST_DRINK_TICK, oldDrinkTicks)
        }
    }

    //玩家登录时同步一次数据
    @SubscribeEvent
    fun onPlayerLogin(event: PlayerEvent.PlayerLoggedInEvent) {
        val player = event.entity as ServerPlayer
        val cap = player.getCapability(FallacyCapabilities.THIRST)!!
        PacketDistributor.sendToPlayer(player, ThirstSyncPacket(cap.value))
    }

    internal fun handleThirstSyncPacket(data: ThirstSyncPacket, context: IPayloadContext) {
        context.player().getCapability(FallacyCapabilities.THIRST)!!.value = data.value
        Fallacy.LOGGER.info("Syncing thirst.")
    }

    internal fun handleDrinkInWorldPacket(data: DrinkInWorldPacket, context: IPayloadContext) {
        val player = context.player() as ServerPlayer
        val result = Drink.attemptDrink(player.level(), player)
        if (result.shouldSwing()) player.swing(InteractionHand.MAIN_HAND, true)
    }
}