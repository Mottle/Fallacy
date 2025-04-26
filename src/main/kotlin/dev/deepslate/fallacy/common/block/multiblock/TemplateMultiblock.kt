package dev.deepslate.fallacy.common.block.multiblock

import dev.deepslate.fallacy.util.RotationHelper
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Vec3i
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate

abstract class TemplateMultiblock(
    override val id: ResourceLocation,
    val relativeMaster: BlockPos,
    val relativeTrigger: BlockPos
) : Multiblock {

//    val template: TemplateData by lazy { getTemplate(level) }

    override fun isBlockTrigger(state: BlockState, direction: Direction, level: Level): Boolean {
        val rotation = try {
            RotationHelper.getRotationBetweenFacing(Direction.NORTH, direction.opposite)
        } catch (_: IllegalArgumentException) {
            return false
        }
        TODO()
    }

    override fun createStructure(level: Level, pos: BlockPos, direction: Direction, by: Player): Boolean {
        val rotation = RotationHelper.getRotationBetweenFacing(Direction.NORTH, direction.opposite)
        TODO()
    }

    override fun getStructure(level: Level): List<StructureTemplate.StructureBlockInfo> {
        TODO("Not yet implemented")
    }

    override fun getSize(level: Level): Vec3i {
        TODO("Not yet implemented")
    }

    fun ensureTemplate(level: ServerLevel) {
        val template =
            level.structureManager.get(id).orElse(null) ?: throw RuntimeException("Template $id not found")
        val blocks = template.palettes[0].blocks()
        val blocksWithoutAir = blocks.filter { s -> !s.state.isAir }
        val trigger = blocks.firstOrNull { info -> info.pos == relativeTrigger }?.state
            ?: throw RuntimeException("Template $id does not contain trigger block at $relativeTrigger")
    }

    private fun getTemplate(level: ServerLevel): TemplateData = TODO()

    data class TemplateData(
        val template: StructureTemplate,
        val blocks: List<StructureTemplate.StructureBlockInfo>,
        val trigger: BlockState
    )
}