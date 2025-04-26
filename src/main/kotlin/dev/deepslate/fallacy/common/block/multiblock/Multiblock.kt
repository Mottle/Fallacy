package dev.deepslate.fallacy.common.block.multiblock

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Vec3i
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate

interface Multiblock {
    val id: ResourceLocation

    fun isBlockTrigger(state: BlockState, direction: Direction, level: Level): Boolean

    fun createStructure(level: Level, pos: BlockPos, direction: Direction, by: Player): Boolean

    fun getStructure(level: Level): List<StructureTemplate.StructureBlockInfo>

    // val renderScala: Float

    fun getSize(level: Level): Vec3i

    fun destroy(level: Level, pos: BlockPos, mirrored: Boolean, direction: Direction)

    val triggerOffset: BlockPos

    val block: Block
        get() = Blocks.AIR
}