package dev.deepslate.fallacy.common.block

import com.mojang.serialization.MapCodec
import dev.deepslate.fallacy.common.block.entity.BurningLogEntity
import dev.deepslate.fallacy.common.block.entity.FallacyBlockEntities
import dev.deepslate.fallacy.util.TickHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.registries.Registries
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn

class BurningLog(properties: Properties) : BaseEntityBlock(properties) {

    companion object {
        val CODEC: MapCodec<BurningLog> = simpleCodec(::BurningLog)
    }

    override fun codec(): MapCodec<out BaseEntityBlock> = CODEC

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? =
        FallacyBlockEntities.BURNING_LOG.create(pos, state)

    override fun getRenderShape(state: BlockState): RenderShape = RenderShape.MODEL

    //client
    @OnlyIn(value = Dist.CLIENT)
    override fun animateTick(state: BlockState, level: Level, pos: BlockPos, random: RandomSource) {
        if (!level.getBlockState(pos.above(2)).canBeReplaced()) return

        cookEffect(state, level, pos, random)
    }

    //client
    @OnlyIn(value = Dist.CLIENT)
    private fun cookEffect(state: BlockState, level: Level, pos: BlockPos, random: RandomSource) {
        val x = pos.x + random.nextDouble()
        val y = pos.y + 1.125
        val z = pos.z + random.nextDouble()
        val smokeYSpeed = 0.1 + 0.1 * random.nextFloat()
        val campfireSmokeXSpeed = (0.5 - random.nextDouble()) / 10
        val campfireSmokeYSpeed = 0.1 + random.nextDouble() / 8
        val campfireSmokeZSpeed = (0.5F - random.nextDouble()) / 10

        level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, smokeYSpeed, 0.0)
        level.addParticle(
            ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, campfireSmokeXSpeed, campfireSmokeYSpeed, campfireSmokeZSpeed
        )

        if (random.nextInt(12) == 0) {
            val volume = 0.5f + random.nextFloat()
            val pitch = random.nextFloat() * 0.7f + 0.6f
            level.playLocalSound(
                x, y, z, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, volume, pitch, false
            )

        }
    }

    override fun stepOn(level: Level, pos: BlockPos, state: BlockState, entity: Entity) {
        if (TickHelper.checkServerTickRate(2)) return
        if (entity !is LivingEntity) return

        val frostWalker =
            level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FROST_WALKER)
        if (EnchantmentHelper.getEnchantmentLevel(frostWalker, entity) > 0) return

        entity.hurt(level.damageSources().hotFloor(), 1f)
    }

    override fun <T : BlockEntity> getTicker(level: Level, state: BlockState, blockEntityType: BlockEntityType<T>)
            : BlockEntityTicker<T>? {
        if (level.isClientSide) return null
        return createTickerHelper(blockEntityType, FallacyBlockEntities.BURNING_LOG.get(), BurningLogEntity::serverTick)
    }
}