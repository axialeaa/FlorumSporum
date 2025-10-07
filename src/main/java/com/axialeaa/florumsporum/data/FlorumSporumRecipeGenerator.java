package com.axialeaa.florumsporum.data;

import com.axialeaa.florumsporum.data.registry.FlorumSporumRecipes;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

public class FlorumSporumRecipeGenerator extends RecipeGenerator {

    public FlorumSporumRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
        super(registries, exporter);
    }

    @Override
    public void generate() {
        Item input = Items.SPORE_BLOSSOM;
        this.createShapeless(RecipeCategory.MISC, Items.PINK_DYE)
            .input(input)
            .group("pink_dye")
            .criterion(hasItem(input), this.conditionsFromItem(input))
            .offerTo(this.exporter, FlorumSporumRecipes.PINK_DYE_RECIPE_KEY);
    }

}