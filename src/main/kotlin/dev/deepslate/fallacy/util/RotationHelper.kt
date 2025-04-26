package dev.deepslate.fallacy.util

import net.minecraft.core.Direction
import net.minecraft.world.level.block.Rotation

object RotationHelper {
//    fun getRotationBetweenFacing(origin: Direction, to: Direction): Rotation {
//        if (to == origin) return Rotation.NONE
//        if (origin.axis == Direction.Axis.Y || to.axis == Direction.Axis.Y)
//            throw IllegalArgumentException("Can't rotate around Y axis")
//
//        var origin = origin.clockWise
//
//        if (origin == to) return Rotation.CLOCKWISE_90
//
//        origin = origin.clockWise
//
//        if (origin == to) return Rotation.CLOCKWISE_180
//
//        origin = origin.clockWise
//
//        if (origin == to) return Rotation.COUNTERCLOCKWISE_90
//
//        throw IllegalStateException("This shouldn't ever happen")
//    }

    fun getRotationBetweenFacing(origin: Direction, to: Direction): Rotation {
        if (to == origin) return Rotation.NONE
        require(origin.axis != Direction.Axis.Y && to.axis != Direction.Axis.Y) {
            "Can't rotate around Y axis"
        }

        val rotations = listOf(
            Rotation.CLOCKWISE_90,
            Rotation.CLOCKWISE_180,
            Rotation.COUNTERCLOCKWISE_90
        )

        val targetIndex = generateSequence(origin.clockWise) { it.clockWise }
            .take(3)
            .indexOfFirst { it == to }

        return rotations.getOrNull(targetIndex)
            ?: throw IllegalStateException("This shouldn't ever happen")
    }
}