package dev.deepslate.fallacy.common.item.armor

import dev.deepslate.fallacy.Fallacy
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.ArmorItem
import net.minecraft.world.item.ArmorMaterial
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.bus.api.IEventBus
import net.neoforged.neoforge.registries.DeferredRegister

object FallacyArmorMaterials {

    private val registry = DeferredRegister.create(BuiltInRegistries.ARMOR_MATERIAL, Fallacy.MOD_ID)

    fun init(bus: IEventBus) {
        registry.register(bus)
    }

    val Rock = registry.register("rock") { _ ->
        ArmorMaterial(
            mapOf(
                ArmorItem.Type.BOOTS to 8,
                ArmorItem.Type.LEGGINGS to 8,
                ArmorItem.Type.CHESTPLATE to 8,
                ArmorItem.Type.HELMET to 8,
                ArmorItem.Type.BODY to 8,
            ), 12, SoundEvents.ARMOR_EQUIP_NETHERITE, { Ingredient.of(Items.AIR) }, emptyList(), 0.0f, 0.0f
        )
    }

    val BROKEN_ROCK = registry.register("broken_rock") { _ ->
        ArmorMaterial(
            mapOf(
                ArmorItem.Type.BOOTS to 0,
                ArmorItem.Type.LEGGINGS to 0,
                ArmorItem.Type.CHESTPLATE to 0,
                ArmorItem.Type.HELMET to 0,
                ArmorItem.Type.BODY to 0,
            ), 0, SoundEvents.ARMOR_EQUIP_NETHERITE, { Ingredient.of(Items.AIR) }, emptyList(), 0.0f, 0.0f
        )
    }
}