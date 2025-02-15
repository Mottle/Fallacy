package dev.deepslate.fallacy.common.item.items

import com.tterrag.registrate.Registrate
import com.tterrag.registrate.builders.ItemBuilder
import com.tterrag.registrate.util.entry.ItemEntry
import dev.deepslate.fallacy.common.FallacyTabs
import dev.deepslate.fallacy.common.item.*
import dev.deepslate.fallacy.common.item.data.ExtendedProperties
import dev.deepslate.fallacy.common.registrate.REG
import dev.deepslate.fallacy.common.registrate.defaultModelWithTexture
import dev.deepslate.fallacy.common.registrate.formattedLang
import dev.deepslate.fallacy.util.CriterionHelper
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.*

object ToolItems {
    val FLINT_PICKAXE: ItemEntry<FallacyPickaxeItem> = REG.item("flint_pickaxe") {
        FallacyPickaxeItem(
            FallacyTiers.FLINT,
            Item.Properties().attributes(PickaxeItem.createAttributes(FallacyTiers.FLINT, 1.0f, -2.8F)),
            ExtendedProperties.Builder().withRank(0)
        )
    }.withDefaultToolTextureModel()
        .recipe { ctx, prov ->
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ctx.entry)
                .pattern("###")
                .pattern(" * ")
                .pattern(" * ")
                .define('#', Items.FLINT).define('*', Items.STICK)
                .unlockedBy("has_stick", CriterionHelper.has(Items.STICK))
                .save(prov)
        }
        .formattedLang()
        .tag(ItemTags.BREAKS_DECORATED_POTS, ItemTags.CLUSTER_MAX_HARVESTABLES, ItemTags.PICKAXES)
        .tab(FallacyTabs.TOOL.key!!).register()

    val FLINT_AXE: ItemEntry<FallacyAxeItem> = REG.item("flint_axe") {
        FallacyAxeItem(
            FallacyTiers.FLINT,
            Item.Properties().attributes(AxeItem.createAttributes(FallacyTiers.FLINT, 6.0f, -3.2F)),
            ExtendedProperties.Builder().withRank(0)
        )
    }.withDefaultToolTextureModel()
        .recipe { ctx, prov ->
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ctx.entry)
                .pattern(" ##")
                .pattern(" *#")
                .pattern(" * ")
                .define('#', Items.FLINT).define('*', Items.STICK)
                .unlockedBy("has_stick", CriterionHelper.has(Items.STICK))
                .save(prov)
        }
        .formattedLang()
        .tag(ItemTags.AXES, ItemTags.BREAKS_DECORATED_POTS)
        .tab(FallacyTabs.TOOL.key!!).register()

    val FLINT_SHOVEL: ItemEntry<FallacyShovelItem> = REG.item("flint_shovel") {
        FallacyShovelItem(
            FallacyTiers.FLINT,
            Item.Properties().attributes(ShovelItem.createAttributes(FallacyTiers.FLINT, 1.5f, -3.0F)),
            ExtendedProperties.Builder().withRank(0)
        )
    }.withDefaultToolTextureModel()
        .recipe { ctx, prov ->
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ctx.entry)
                .pattern(" # ")
                .pattern(" * ")
                .pattern(" * ")
                .define('#', Items.FLINT).define('*', Items.STICK)
                .unlockedBy("has_stick", CriterionHelper.has(Items.STICK))
                .save(prov)
        }
        .formattedLang()
        .tag(ItemTags.BREAKS_DECORATED_POTS, ItemTags.SHOVELS)
        .tab(FallacyTabs.TOOL.key!!).register()

    val FLINT_HOE: ItemEntry<FallacyHoeItem> = REG.item("flint_hoe") {
        FallacyHoeItem(
            FallacyTiers.FLINT,
            Item.Properties().attributes(HoeItem.createAttributes(FallacyTiers.FLINT, 0.0f, -3.0F)),
            ExtendedProperties.Builder().withRank(0)
        )
    }.withDefaultToolTextureModel()
        .recipe { ctx, prov ->
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ctx.entry)
                .pattern(" ##")
                .pattern(" * ")
                .pattern(" * ")
                .define('#', Items.FLINT).define('*', Items.STICK)
                .unlockedBy("has_stick", CriterionHelper.has(Items.STICK))
                .save(prov)
        }
        .formattedLang()
        .tag(ItemTags.HOES, ItemTags.BREAKS_DECORATED_POTS)
        .tab(FallacyTabs.TOOL.key!!).register()

    val BONE_PICKAXE: ItemEntry<FallacyPickaxeItem> = REG.item("bone_pickaxe") {
        FallacyPickaxeItem(
            FallacyTiers.BONE,
            Item.Properties().attributes(PickaxeItem.createAttributes(FallacyTiers.BONE, 1.0f, -2.6F)),
            ExtendedProperties.Builder().withRank(0)
        )
    }.withDefaultToolTextureModel()
        .recipe { ctx, prov ->
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ctx.entry)
                .pattern("###")
                .pattern(" * ")
                .pattern(" * ")
                .define('#', Items.BONE).define('*', Items.STICK)
                .unlockedBy("has_stick", CriterionHelper.has(Items.STICK))
                .save(prov)
        }
        .formattedLang()
        .tag(ItemTags.BREAKS_DECORATED_POTS, ItemTags.CLUSTER_MAX_HARVESTABLES, ItemTags.PICKAXES)
        .tab(FallacyTabs.TOOL.key!!).register()

    val BONE_AXE: ItemEntry<FallacyAxeItem> = REG.item("bone_axe") {
        FallacyAxeItem(
            FallacyTiers.BONE,
            Item.Properties().attributes(AxeItem.createAttributes(FallacyTiers.BONE, 6.0f, -3.0F)),
            ExtendedProperties.Builder().withRank(0)
        )
    }.withDefaultToolTextureModel()
        .recipe { ctx, prov ->
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ctx.entry)
                .pattern(" ##")
                .pattern(" *#")
                .pattern(" * ")
                .define('#', Items.BONE).define('*', Items.STICK)
                .unlockedBy("has_stick", CriterionHelper.has(Items.STICK))
                .save(prov)
        }
        .formattedLang()
        .tag(ItemTags.AXES, ItemTags.BREAKS_DECORATED_POTS)
        .tab(FallacyTabs.TOOL.key!!).register()

    val BONE_SHOVEL: ItemEntry<FallacyShovelItem> = REG.item("bone_shovel") {
        FallacyShovelItem(
            FallacyTiers.BONE,
            Item.Properties().attributes(ShovelItem.createAttributes(FallacyTiers.BONE, 1.5f, -2.8F)),
            ExtendedProperties.Builder().withRank(0)
        )
    }.withDefaultToolTextureModel()
        .recipe { ctx, prov ->
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ctx.entry)
                .pattern(" # ")
                .pattern(" * ")
                .pattern(" * ")
                .define('#', Items.BONE).define('*', Items.STICK)
                .unlockedBy("has_stick", CriterionHelper.has(Items.STICK))
                .save(prov)
        }
        .formattedLang()
        .tag(ItemTags.BREAKS_DECORATED_POTS, ItemTags.SHOVELS)
        .tab(FallacyTabs.TOOL.key!!).register()

    val BONE_HOE: ItemEntry<FallacyHoeItem> = REG.item("bone_hoe") {
        FallacyHoeItem(
            FallacyTiers.BONE,
            Item.Properties().attributes(HoeItem.createAttributes(FallacyTiers.BONE, 0.0f, -2.8F)),
            ExtendedProperties.Builder().withRank(0)
        )
    }.withDefaultToolTextureModel()
        .recipe { ctx, prov ->
            ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ctx.entry)
                .pattern(" ##")
                .pattern(" * ")
                .pattern(" * ")
                .define('#', Items.BONE).define('*', Items.STICK)
                .unlockedBy("has_stick", CriterionHelper.has(Items.STICK))
                .save(prov)
        }
        .formattedLang()
        .tag(ItemTags.HOES, ItemTags.BREAKS_DECORATED_POTS)
        .tab(FallacyTabs.TOOL.key!!).register()

    private fun <T : Item> ItemBuilder<T, Registrate>.withDefaultToolTextureModel(): ItemBuilder<T, Registrate> {
        return this.defaultModelWithTexture("tool/${this.name}")
    }
}