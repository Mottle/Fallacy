package dev.deepslate.fallacy.thermodynamics

import dev.deepslate.fallacy.thermodynamics.data.HeatLayer
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.CampfireBlock
import net.minecraft.world.level.block.state.BlockState

object VanillaHeat {
    private val heatSet = setOf(
        Blocks.LAVA,
        Blocks.LAVA_CAULDRON,
        Blocks.MAGMA_BLOCK,
        Blocks.FIRE,
        Blocks.SOUL_FIRE,
        Blocks.CAMPFIRE,
        Blocks.SOUL_CAMPFIRE,

        Blocks.SNOW,
        Blocks.SNOW_BLOCK,
        Blocks.ICE,
        Blocks.BLUE_ICE,
        Blocks.PACKED_ICE,
        Blocks.POWDER_SNOW,
        Blocks.POWDER_SNOW_CAULDRON,
    )

    fun hasHeat(state: BlockState): Boolean {
        val block = state.block
        if (block !in heatSet) return false
        if (block is CampfireBlock) return state.getValue(CampfireBlock.LIT)
        return true
    }

    fun getHeat(state: BlockState): UInt {
        val block = state.block
        if (block == Blocks.LAVA) return ThermodynamicsEngine.fromFreezingPoint(1300)
        if (block == Blocks.MAGMA_BLOCK) return ThermodynamicsEngine.fromFreezingPoint(600)
        if (block == Blocks.FIRE) return ThermodynamicsEngine.fromFreezingPoint(340)
        if (block == Blocks.SOUL_FIRE) return ThermodynamicsEngine.fromFreezingPoint(680)
        if (block == Blocks.CAMPFIRE && state.getValue(CampfireBlock.LIT)) return ThermodynamicsEngine.fromFreezingPoint(
            340
        )
        if (block == Blocks.SOUL_CAMPFIRE && state.getValue(CampfireBlock.LIT)) return ThermodynamicsEngine.fromFreezingPoint(
            680
        )

        if (block == Blocks.SNOW) return ThermodynamicsEngine.fromFreezingPoint(-10)
        if (block == Blocks.SNOW_BLOCK) return ThermodynamicsEngine.fromFreezingPoint(-10)
        if (block == Blocks.ICE) return ThermodynamicsEngine.fromFreezingPoint(-20)
        if (block == Blocks.BLUE_ICE) return ThermodynamicsEngine.fromFreezingPoint(-30)
        if (block == Blocks.PACKED_ICE) return ThermodynamicsEngine.fromFreezingPoint(-40)
        if (block == Blocks.POWDER_SNOW) return ThermodynamicsEngine.fromFreezingPoint(-10)
        if (block == Blocks.POWDER_SNOW_CAULDRON) return ThermodynamicsEngine.fromFreezingPoint(-10)

        return HeatLayer.FREEZING_POINT
    }
}