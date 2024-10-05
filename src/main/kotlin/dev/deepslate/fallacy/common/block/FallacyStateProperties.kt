package dev.deepslate.fallacy.common.block

import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.state.properties.IntegerProperty

object FallacyStateProperties {
    val AGE = CropBlock.AGE

    val DYING_COUNTER = IntegerProperty.create("dying_counter", 0, 3)

    val N = IntegerProperty.create("nitrogen", 0, 7)

    val P = IntegerProperty.create("phosphorus", 0, 7)

    val K = IntegerProperty.create("potassium", 0, 7)

    val PART2 = IntegerProperty.create("part", 0, 1)
}