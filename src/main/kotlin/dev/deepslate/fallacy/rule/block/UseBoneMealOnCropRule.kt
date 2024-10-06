package dev.deepslate.fallacy.rule.block

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.block.crop.FallacyCropBlock
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.BonemealEvent

@EventBusSubscriber(modid = Fallacy.MOD_ID)
object UseBoneMealOnCropRule {
    @SubscribeEvent
    fun onBoneMeal(event: BonemealEvent) {
        val block = event.state.block
        if (block is FallacyCropBlock && !block.canGrowByBoneMeal) event.isCanceled = true
    }
}