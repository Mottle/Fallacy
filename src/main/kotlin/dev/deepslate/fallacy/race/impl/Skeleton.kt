package dev.deepslate.fallacy.race.impl

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.behavior.Behavior
import dev.deepslate.fallacy.behavior.Behaviors
import dev.deepslate.fallacy.common.capability.FallacyCapabilities
import dev.deepslate.fallacy.common.capability.Synchronous
import dev.deepslate.fallacy.common.capability.skeleton.ISkeleton
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.common.data.player.NutritionState
import dev.deepslate.fallacy.common.data.player.PlayerAttribute
import dev.deepslate.fallacy.common.network.packet.BoneSyncPacket
import dev.deepslate.fallacy.race.Race
import dev.deepslate.fallacy.race.Races
import dev.deepslate.fallacy.race.Respawnable
import dev.deepslate.fallacy.race.resources.AttributeResource
import dev.deepslate.fallacy.race.resources.NutritionResource
import dev.deepslate.fallacy.util.EntityHelper
import dev.deepslate.fallacy.util.TickHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent
import net.neoforged.neoforge.network.PacketDistributor
import net.neoforged.neoforge.network.handling.IPayloadContext
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.sqrt

class Skeleton : Race(), Respawnable {

    companion object {
        val ID = Fallacy.withID("skeleton")

        val TAGS = listOf(
            Behaviors.UNDEAD, Behaviors.WEAKNESS_2_IN_SUNLIGHT,
            Behaviors.BURNING_IN_SUNLIGHT
        )

        internal fun internalKill(player: Player, source: DamageSource?) {
            val access = player.registryAccess().lookup(Registries.DAMAGE_TYPE).get()
            val type = access.get(DamageTypes.GENERIC_KILL).get()
            val dieSource = DamageSource(type, source?.entity)
            player.hurt(dieSource, 114514f)
        }
    }

    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object DamageHandler {
        @SubscribeEvent
        fun onDamage(event: LivingDamageEvent.Pre) {
            if (event.entity !is Player) return

            val cap = event.entity.getCapability(FallacyCapabilities.SKELETON) ?: return
            val player = event.entity as Player
            val race = Race.get(player) as Skeleton
            val damage = event.newDamage - 4

            if (player.isInvulnerable) return
            if (checkDamage(event.source)) return

            fixDamage(event)

            val newBone = loseBone(cap, player, damage)

            if (player is ServerPlayer) {
                race.syncBone(player)
            }

            if (newBone <= 0f && player.isAlive) internalKill(player, event.source)
        }

        private fun loseBone(cap: ISkeleton, player: Player, fixedDamage: Float): Float {
            if (fixedDamage <= 0f) return cap.bone

            val realArmor = player.getAttributeValue(Attributes.ARMOR).toFloat()
            val prob = getProbability(realArmor, fixedDamage)
            val beforeLoss = floor(sqrt(max(0f, fixedDamage)))
            val boneLoss = if (player.random.nextIntBetweenInclusive(0, 99) < prob) beforeLoss else 0f
            cap.bone -= boneLoss
            return cap.bone
        }


        private fun getProbability(armor: Float, damage: Float): Float {
            val fixedArmor = max(0f, armor)
            val negativeArmorFixed = if (armor < 0f) armor * 5f else 0f
            val prob = damage / (2f * fixedArmor + 10f) * 100f - negativeArmorFixed
            return max(10f, prob)
        }

        private fun fixDamage(event: LivingDamageEvent.Pre) {
            event.newDamage = 0f
        }

        private val passByDamageSet =
            setOf(DamageTypes.FELL_OUT_OF_WORLD, DamageTypes.OUTSIDE_BORDER, DamageTypes.GENERIC_KILL)

        private fun checkDamage(damage: DamageSource): Boolean = passByDamageSet.any { damage.`is`(it) }
    }

    @EventBusSubscriber(modid = Fallacy.MOD_ID)
    object Handler {
        //client side
        internal fun handleBoneSync(data: BoneSyncPacket, context: IPayloadContext) {
            context.player().setData(FallacyAttachments.BONE, data.bone)
            Fallacy.LOGGER.info("Syncing bone.")
        }

        @SubscribeEvent
        fun onPlayerJoin(event: PlayerEvent.PlayerLoggedInEvent) {
//            val race = Race.get(event.entity)
//            if (race !is Skeleton) return
//            race.syncBone(event.entity as ServerPlayer)
            val cap = event.entity.getCapability(FallacyCapabilities.SKELETON) ?: return

            if (cap is Synchronous) cap.synchronize()
        }

        private fun check(entity: LivingEntity, item: ItemStack): Boolean {
            if (!item.`is`(Items.BONE)) return false
            if (entity !is Player) return false
            if (!Race.get(entity).`is`(Races.SKELETON)) return false
            return true
        }

        @SubscribeEvent
        fun onPlayerRightClickItem(event: PlayerInteractEvent.RightClickItem) {
            if (!check(event.entity, event.itemStack)) return
            val player = event.entity as Player
            val cap = player.getCapability(FallacyCapabilities.SKELETON) ?: return
            if (cap.bone >= cap.max) return

            player.startUsingItem(event.hand)
        }

        @SubscribeEvent
        fun onUseBoneStart(event: LivingEntityUseItemEvent.Start) {
            if (!check(event.entity, event.item)) return

            val player = event.entity as Player
            event.duration = if (player.combatTracker.inCombat) TickHelper.second(5) else TickHelper.second(1)
        }

        @SubscribeEvent
        fun onUseBoneTick(event: LivingEntityUseItemEvent.Tick) {
            if (!check(event.entity, event.item)) return

            val player = event.entity as Player
            val item = event.item
            player.spawnItemParticles(item, 5)
            player.playSound(
                player.getEatingSound(item),
                0.5F + 0.5F * player.random.nextInt(2),
                (player.random.nextFloat() - player.random.nextFloat()) * 0.2F + 1.0F
            )
        }

        @SubscribeEvent
        fun onUseBoneFinish(event: LivingEntityUseItemEvent.Finish) {
            if (!check(event.entity, event.item)) return

            val player = event.entity as Player
            val cap = player.getCapability(FallacyCapabilities.SKELETON) ?: return

            event.resultStack.count--
            cap.bone += 1 + if (player.combatTracker.inCombat) 0f else 1f
            player.swing(player.usedItemHand)
        }
    }

    override val namespacedId: ResourceLocation = ID

//    override val attribute: PlayerAttribute =
//        PlayerAttribute(armor = -5.0, attackDamage = 6.0, strength = 1.0, magicResistance = 40.0, health = 20.0)
//
//    override val nutrition: NutritionState = NutritionState.noNeed()

    override val resources: Map<String, Resource> = mapOf(
        AttributeResource.KEY to AttributeResource(
            PlayerAttribute(
                armor = -5.0,
                attackDamage = 6.0,
                strength = 1.0,
                magicResistance = 40.0,
                health = 20.0
            )
        ),
        NutritionResource.KEY to NutritionResource(NutritionState.noNeed())
    )

    fun syncBone(serverPlayer: ServerPlayer) {
        val bone = serverPlayer.getData(FallacyAttachments.BONE)
        PacketDistributor.sendToPlayer(serverPlayer, BoneSyncPacket(bone))
    }

    override fun tick(
        level: ServerLevel,
        player: ServerPlayer,
        position: BlockPos
    ) {
        player.foodData.foodLevel = max(10, player.foodData.foodLevel)
        val cap = player.getCapability(FallacyCapabilities.SKELETON) ?: return

        if (player.isInvulnerable) return
        if (EntityHelper.checkUndeadBurning(level, player, position)) {
            cap.bone -= 0.5f

            if (cap is Synchronous) cap.synchronize()

            EntityHelper.damageHead(player, 2)
        }

        if (cap.bone <= 0f && player.isAlive) internalKill(player, player.lastDamageSource)

    }

    override fun apply(player: ServerPlayer) {
        Behavior.addAll(player, TAGS)
        syncBone(player)
    }

    override fun deapply(player: ServerPlayer) {
        Behavior.removeAll(player, TAGS)
    }

    override fun onRespawn(
        player: ServerPlayer
    ) {
        syncBone(player)
    }
}