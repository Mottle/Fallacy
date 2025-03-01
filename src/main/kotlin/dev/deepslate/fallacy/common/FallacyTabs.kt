package dev.deepslate.fallacy.common

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.common.item.FallacyItems
import dev.deepslate.fallacy.common.registrate.REG
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Items
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister


object FallacyTabs {
    private val registry: DeferredRegister<CreativeModeTab> =
        DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, Fallacy.MOD_ID)

    fun init(bus: IEventBus) {
        registry.register(bus)
        //Registrate.defaultCreativeModeTab默认指向Search，之后在item注册时使用tab会导致重复注册，在此将其置null避免此问题
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        REG.defaultCreativeTab(null as (ResourceKey<CreativeModeTab>)?)
    }

    private fun simpleRegister(
        name: String,
        factory: (CreativeModeTab.Builder) -> CreativeModeTab
    ): DeferredHolder<CreativeModeTab, CreativeModeTab> =
        registry.register(name) { _ -> factory(CreativeModeTab.builder()) }

    val OTHER = simpleRegister("other") { builder ->
        builder.title(REG.addLang("itemGroup", Fallacy.withID("other"), "Fallacy: Other"))
            .icon { Items.STONE.defaultInstance }.build()
    }

    val TOOL = simpleRegister("tool") { builder ->
        builder.title(REG.addLang("itemGroup", Fallacy.withID("tool"), "Fallacy: Tool"))
            .icon { Items.IRON_PICKAXE.defaultInstance }.build()
    }

    val NATURE = simpleRegister("nature") { builder ->
        builder.title(REG.addLang("itemGroup", Fallacy.withID("nature"), "Fallacy: Nature"))
            .icon { FallacyItems.MIU_BERRIES.asStack() }.build()
    }

    val GEOLOGY = simpleRegister("geology") { builder ->
        builder.title(REG.addLang("itemGroup", Fallacy.withID("geology"), "Fallacy: Geology"))
            .icon { FallacyItems.GEOLOGY.GNEISS.asStack() }.build()
    }

    val FARMING = simpleRegister("farming") { builder ->
        builder.title(REG.addLang("itemGroup", Fallacy.withID("farming"), "Fallacy: Farming"))
            .icon { FallacyItems.CROP.BARLEY.asStack() }.build()
    }

    val MATERIAl = simpleRegister("material") { builder ->
        builder.title(REG.addLang("itemGroup", Fallacy.withID("material"), "Fallacy: Material"))
            .icon { FallacyItems.MATERIAL.FOSSIL_FRAGMENT.asStack() }.build()
    }
}