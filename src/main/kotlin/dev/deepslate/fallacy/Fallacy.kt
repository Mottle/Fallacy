package dev.deepslate.fallacy

import dev.deepslate.fallacy.behavior.Behaviors
import dev.deepslate.fallacy.common.FallacyTabs
import dev.deepslate.fallacy.common.block.FallacyBlocks
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.common.data.FallacyAttributes
import dev.deepslate.fallacy.common.effect.FallacyEffects
import dev.deepslate.fallacy.common.item.FallacyItems
import dev.deepslate.fallacy.common.item.armor.FallacyArmorMaterials
import dev.deepslate.fallacy.common.item.component.FallacyDataComponents
import dev.deepslate.fallacy.common.loot.FallacyLootModifiers
import dev.deepslate.fallacy.common.registrate.Registration
import dev.deepslate.fallacy.race.Races
import dev.deepslate.fallacy.rule.RangedAttributeRule
import dev.deepslate.fallacy.rule.item.VanillaExtendedFoodPropertiesRule
import dev.deepslate.fallacy.rule.item.VanillaItemDeprecationRule
import dev.deepslate.fallacy.util.region.RegionTypes
import dev.deepslate.fallacy.weather.Weathers
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(Fallacy.MOD_ID)
class Fallacy(val modBus: IEventBus) {
    companion object {
        const val MOD_ID = "fallacy"

        val LOGGER: Logger = LogManager.getLogger(MOD_ID)

        fun id(name: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, name)
    }

    init {
        modBus.addListener(::commonSetup)
        Registration.init()
        RegionTypes.init(modBus)
        FallacyAttachments.register(modBus)

        FallacyLootModifiers.init(modBus)
        FallacyTabs.init(modBus)
        FallacyAttributes.init(modBus)
        FallacyEffects.init(modBus)
        Behaviors.init(modBus)
        Races.init(modBus)
        FallacyDataComponents.init(modBus)
        FallacyArmorMaterials.init(modBus)
        Weathers.init(modBus)

        FallacyItems
        FallacyBlocks

        playRule()
    }

    @SubscribeEvent
    fun commonSetup(event: FMLCommonSetupEvent) {
    }

    private fun playRule() {
        RangedAttributeRule.rule()
        VanillaExtendedFoodPropertiesRule.rule()
        VanillaItemDeprecationRule.rule()
        LOGGER.info("Rules load over.")
    }
}