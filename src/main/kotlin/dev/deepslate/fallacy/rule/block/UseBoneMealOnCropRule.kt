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
        if (event.state.block is FallacyCropBlock) event.isCanceled = true
    }
}