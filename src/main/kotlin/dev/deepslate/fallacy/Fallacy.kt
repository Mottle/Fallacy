package dev.deepslate.fallacy

import dev.deepslate.fallacy.common.FallacyTabs
import dev.deepslate.fallacy.common.block.FallacyBlocks
import dev.deepslate.fallacy.common.data.FallacyAttachments
import dev.deepslate.fallacy.common.data.FallacyAttributes
import dev.deepslate.fallacy.common.effect.FallacyEffects
import dev.deepslate.fallacy.common.item.FallacyItems
import dev.deepslate.fallacy.common.item.armor.FallacyArmorMaterials
import dev.deepslate.fallacy.common.item.data.FallacyDataComponents
import dev.deepslate.fallacy.common.registrate.Registration
import dev.deepslate.fallacy.race.FallacyRaces
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
        FallacyAttachments.register(modBus)

        FallacyTabs.init(modBus)
        FallacyAttributes.init(modBus)
        FallacyEffects.init(modBus)
        FallacyRaces.init(modBus)
        FallacyDataComponents.init(modBus)
        FallacyArmorMaterials.init(modBus)

        FallacyItems
        FallacyBlocks
    }

    @SubscribeEvent
    fun commonSetup(event: FMLCommonSetupEvent) {
    }
}