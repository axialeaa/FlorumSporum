package com.axialeaa.florumsporum.data.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

import net.minecraft.data.recipe.*;

import static com.axialeaa.florumsporum.data.recipe.RecipeConstants.*;

public class FlorumSporumRecipeGenerator extends RecipeGenerator {

    public FlorumSporumRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
        super(registries, exporter);
    }

    @Override
    public void generate() {
        Item sporeBlossom = Items.SPORE_BLOSSOM;
        this.createShapeless(RecipeCategory.MISC, Items.PINK_DYE)
            .input(sporeBlossom)
            .group(PINK_DYE_GROUP)
            .criterion(HAS_SPORE_BLOSSOM, this.conditionsFromItem(sporeBlossom))
            .offerTo(this.exporter, PINK_DYE_RECIPE_KEY);
    }

}