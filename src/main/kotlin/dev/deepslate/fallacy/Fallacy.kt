package dev.deepslate.fallacy

//import dev.deepslate.fallacy.client.FallacyClient
//import dev.deepslate.fallacy.common.FallacyTabs
//import dev.deepslate.fallacy.common.block.FallacyBlocks
//import dev.deepslate.fallacy.common.blockentity.FallacyBlockEntities
//import dev.deepslate.fallacy.common.Attachments
//import dev.deepslate.fallacy.common.fluid.FallacyFluidTypes
//import dev.deepslate.fallacy.common.handler.Handlers
//import dev.deepslate.fallacy.common.item.FallacyItems
//import dev.deepslate.fallacy.common.misc.metal.Metals
//import dev.deepslate.fallacy.common.misc.rock.Rocks
//import dev.deepslate.fallacy.common.recipe.FallacyRecipeSerializers
//import dev.deepslate.fallacy.common.recipe.FallacyRecipes
//import dev.deepslate.fallacy.utils.data.*
import net.neoforged.bus.api.IEventBus
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent
import net.neoforged.neoforge.common.NeoForge
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
//import dev.deepslate.fallacy.player.ethnicity.RegisterHandler as EthnicityRegisterHandler

@Mod(Fallacy.MOD_ID)
class Fallacy(val modBus: IEventBus) {
    companion object {
        const val MOD_ID = "fallacy"

        val LOGGER: Logger = LogManager.getLogger(MOD_ID)
    }

    private fun loadKt() {
//        Metals
//        Rocks
    }

    private fun loadUtils() {
//        modBus.register(LanguageHandler.INSTANCE)
//        modBus.register(ModelHandler.INSTANCE)
//
//        modBus.register(TagHandler.INSTANCE)
//        modBus.register(LootHandler.INSTANCE)
//        modBus.register(RecipeHandler.INSTANCE)
    }

    private fun disposeUtils() {
//        LanguageHandler.dispose()
//        ModelHandler.dispose()
//
//        TagHandler.dispose()
//        LootHandler.dispose()
//        RecipeHandler.dispose()
    }

    init {
        modBus.addListener(::commonSetup)
//        Attachments.register(modBus)
//        NeoForge.EVENT_BUS.register(EthnicityRegisterHandler())
//        loadKt()
//        FallacyTabs.CREATIVE_TABS.register(modBus)
//        FallacyItems.ITEMS.register(modBus)
//        FallacyBlocks.BLOCKS.register(modBus)
//        FallacyBlockEntities.BLOCK_ENTITIES.register(modBus)
//        FallacyRecipeSerializers.SERIALIZER.register(modBus)
//        FallacyRecipes.RECIPES.register(modBus)
//        FallacyFluidTypes.FLUID_TYPES.register(modBus)
//        loadUtils()
//        Vanilla().handle()
//
//        Handlers.register(NeoForge.EVENT_BUS)
//        FallacyClient(modBus)
    }

    @SubscribeEvent
    fun commonSetup(event: FMLCommonSetupEvent) {
//        disposeUtils()
//        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BRICK_PILE.get(), RenderType.cutout())
    }
}