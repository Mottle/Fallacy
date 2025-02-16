package dev.deepslate.fallacy.common.item.items

import com.tterrag.registrate.providers.DataGenContext
import com.tterrag.registrate.providers.RegistrateItemModelProvider
import com.tterrag.registrate.util.entry.ItemEntry
import dev.deepslate.fallacy.common.FallacyTabs
import dev.deepslate.fallacy.common.item.FallacyItem
import dev.deepslate.fallacy.common.item.data.ExtendedProperties
import dev.deepslate.fallacy.common.registrate.REG
import dev.deepslate.fallacy.common.registrate.formattedLang
import net.minecraft.world.item.Item

object MaterialItems {
    val FOSSIL_FRAGMENT: ItemEntry<FallacyItem> =
        REG.item("fossil_fragment") { FallacyItem(it, ExtendedProperties.default()) }
            .formattedLang().model(::withModel).tab(tabKey).register()

    private val tabKey
        get() = FallacyTabs.MATERIAl.key!!

    private fun <T : Item> withModel(
        context: DataGenContext<Item, T>,
        provider: RegistrateItemModelProvider
    ) {
        provider.generated(context::getEntry, provider.modLoc("item/material/${context.name}"))
    }
}