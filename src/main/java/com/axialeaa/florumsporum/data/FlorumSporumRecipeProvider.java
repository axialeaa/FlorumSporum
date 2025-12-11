package com.axialeaa.florumsporum.data;

import com.axialeaa.florumsporum.data.registry.FlorumSporumRecipes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class FlorumSporumRecipeProvider extends RecipeProvider {

    public FlorumSporumRecipeProvider(HolderLookup.Provider holderProvider, RecipeOutput output) {
        super(holderProvider, output);
    }

    @Override
    public void buildRecipes() {
        Item input = Items.SPORE_BLOSSOM;
        this.shapeless(RecipeCategory.MISC, Items.PINK_DYE)
            .requires(input)
            .group("pink_dye")
            .unlockedBy(getHasName(input), this.has(input))
            .save(this.output, FlorumSporumRecipes.PINK_DYE_RECIPE_KEY);
    }

}