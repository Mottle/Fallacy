package dev.deepslate.fallacy.common.block

import net.minecraft.world.level.block.BeetrootBlock
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.state.properties.IntegerProperty

object FallacyStateProperties {
    val AGE: IntegerProperty = CropBlock.AGE

    val AGE_3: IntegerProperty = BeetrootBlock.AGE

    val DYING: IntegerProperty = IntegerProperty.create("dying", 0, 3)

    val N: IntegerProperty = IntegerProperty.create("nitrogen", 0, 7)

    val P: IntegerProperty = IntegerProperty.create("phosphorus", 0, 7)

    val K: IntegerProperty = IntegerProperty.create("potassium", 0, 7)

    val PART_2: IntegerProperty = IntegerProperty.create("part", 0, 1)
}