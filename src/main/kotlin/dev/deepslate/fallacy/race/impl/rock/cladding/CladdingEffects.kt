package dev.deepslate.fallacy.race.impl.rock.cladding

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantments

object CladdingEffects {
    private fun getKey(item: Item): ResourceLocation = BuiltInRegistries.ITEM.getKey(item)

    val claddingEffectMap: Map<ResourceLocation, CladdingContainer> = mapOf(
        getKey(Items.GLASS) to CladdingContainer(
            1, listOf()
        ),
        getKey(Items.IRON_INGOT) to CladdingContainer(
            125,
            listOf(CladdingAttributeModifier(Attributes.ARMOR, 0.2))
        ),
        getKey(Items.COPPER_INGOT) to CladdingContainer(
            100,
            listOf(
                CladdingAttributeModifier(Attributes.ARMOR, 0.2),
                CladdingAttributeModifier(Attributes.MOVEMENT_SPEED, -0.0015)
            )
        ),
        getKey(Items.GOLD_INGOT) to CladdingContainer(
            80, listOf(
                CladdingAttributeModifier(Attributes.MOVEMENT_SPEED, 0.001875),
                CladdingAttributeModifier(Attributes.MAX_HEALTH, -1.5)
            )
        ),
        getKey(Items.DIAMOND) to CladdingContainer(
            600, listOf(
                CladdingAttributeModifier(Attributes.ARMOR, 0.8),
            )
        ),
        getKey(Items.OBSIDIAN) to CladdingContainer(
            1200, listOf(
                CladdingEnchantmentAdder(Enchantments.FIRE_PROTECTION, 1),
                CladdingEnchantmentAdder(Enchantments.BLAST_PROTECTION, 1),
                CladdingAttributeModifier(Attributes.ARMOR, 0.5),
                CladdingAttributeModifier(Attributes.MOVEMENT_SPEED, -0.002)
            ),
            3
        ),
        getKey(Items.NETHERITE_INGOT) to CladdingContainer(
            1500, listOf(
                CladdingAttributeModifier(Attributes.ARMOR, 1.5)
            )
        )
    )

    operator fun get(item: Item) = claddingEffectMap[getKey(item)]

    operator fun get(itemStack: ItemStack) = get(itemStack.item)

    operator fun get(id: ResourceLocation) = claddingEffectMap[id]
}