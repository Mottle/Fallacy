package dev.deepslate.fallacy.common.registrate

import com.tterrag.registrate.builders.ItemBuilder
import net.minecraft.world.item.Item

fun <T : Item, P> ItemBuilder<T, P>.defaultModelWithTexture(texturePath: String): ItemBuilder<T, P> =
    model { ctx, prov ->
        prov.generated(ctx::getEntry, prov.modLoc("item/$texturePath"))
    }

fun <T : Item, P> ItemBuilder<T, P>.defaultModelWithVanillaTexture(texturePath: String): ItemBuilder<T, P> =
    model { ctx, prov ->
        prov.generated(ctx::getEntry, prov.mcLoc("item/$texturePath"))
    }