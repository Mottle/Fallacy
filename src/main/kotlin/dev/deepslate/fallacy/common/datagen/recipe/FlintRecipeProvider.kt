package dev.deepslate.fallacy.common.datagen.recipe

import dev.deepslate.fallacy.Fallacy
import dev.deepslate.fallacy.util.CriterionHelper
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.world.item.Items
import java.util.concurrent.CompletableFuture

class FlintRecipeProvider(output: PackOutput, lookupProvider: CompletableFuture<HolderLookup.Provider>) :
    RecipeProvider(output, lookupProvider) {
    override fun buildRecipes(recipeOutput: RecipeOutput) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.FLINT).requires(Items.DIRT, 3)
            .unlockedBy("has_dirt", CriterionHelper.has(Items.DIRT)).save(recipeOutput, Fallacy.withID("dirt2flint"))

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.FLINT).requires(Items.GRAVEL, 2)
            .unlockedBy("has_gravel", CriterionHelper.has(Items.GRAVEL))
            .save(recipeOutput, Fallacy.withID("gravel2flint"))
    }
}