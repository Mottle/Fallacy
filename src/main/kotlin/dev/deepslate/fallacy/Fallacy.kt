package dev.deepslate.fallacy

import dev.deepslate.fallacy.common.capability.FallacyAttachments
import dev.deepslate.fallacy.common.data.CreativeModeTabs
import dev.deepslate.fallacy.common.data.FallacyBlocks
import dev.deepslate.fallacy.common.data.FallacyDataComponents
import dev.deepslate.fallacy.common.data.FallacyItems
import dev.deepslate.fallacy.common.registrate.Registration
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
//import dev.deepslate.fallacy.player.ethnicity.RegisterHandler as EthnicityRegisterHandler

@Mod(Fallacy.MOD_ID)
class Fallacy(modBus: IEventBus) {
    companion object {
        const val MOD_ID = "fallacy"

        val LOGGER: Logger = LogManager.getLogger(MOD_ID)

        fun id(name: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MOD_ID, name)
    }

    init {
        modBus.addListener(::commonSetup)
        Registration.init()
        FallacyAttachments.register(modBus)

        CreativeModeTabs.init(modBus)
        FallacyItems
        FallacyBlocks
    }

    @SubscribeEvent
    fun commonSetup(event: FMLCommonSetupEvent) {}
}