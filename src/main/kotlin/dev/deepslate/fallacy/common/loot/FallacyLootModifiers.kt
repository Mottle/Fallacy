package dev.deepslate.fallacy.common.loot

import dev.deepslate.fallacy.Fallacy
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries

object FallacyLootModifiers {
    private val registry =
        DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Fallacy.MOD_ID)

    val REPLACE_ITEM = registry.register("replace_item") { _ -> ReplaceItem.CODEC }

    fun init(bus: IEventBus) {
        registry.register(bus)
    }
}