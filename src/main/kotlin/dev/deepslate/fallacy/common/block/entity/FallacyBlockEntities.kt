package dev.deepslate.fallacy.common.block.entity

import com.tterrag.registrate.util.entry.BlockEntityEntry
import com.tterrag.registrate.util.nullness.NonNullFunction
import dev.deepslate.fallacy.client.render.block.TickDurationBlockEntityRenderer
import dev.deepslate.fallacy.common.block.FallacyBlocks
import dev.deepslate.fallacy.common.registrate.REG

object FallacyBlockEntities {
    val BURNING_LOG: BlockEntityEntry<BurningLogEntity> = REG.blockEntity("burning_log", ::BurningLogEntity)
        .validBlocks(FallacyBlocks.BURNING_LOG).renderer {
            NonNullFunction { TickDurationBlockEntityRenderer<BurningLogEntity>(it) }
        }.register()

    val TICK_COUNTER: BlockEntityEntry<TickDurationBlockEntity> =
        REG.blockEntity("tick_counter", ::TickDurationBlockEntity).register()
}