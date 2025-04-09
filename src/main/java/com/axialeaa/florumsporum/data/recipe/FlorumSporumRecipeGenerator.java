//? if >=1.21.3 {
package com.axialeaa.florumsporum.data.recipe;

import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;

//? if 1.21.3 {
/*import net.minecraft.data.server.recipe.*;
*///?} else
import net.minecraft.data.recipe.*;

public class FlorumSporumRecipeGenerator extends RecipeGenerator {

    public FlorumSporumRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
        super(registries, exporter);
    }

    @Override
    public void generate() {
        this.createShapeless(RecipeCategory.MISC, Items.PINK_DYE)
            .input(Items.SPORE_BLOSSOM)
            .group(RecipeConstants.PINK_DYE)
            .criterion(RecipeConstants.HAS_SPORE_BLOSSOM, this.conditionsFromItem(Items.SPORE_BLOSSOM))
            .offerTo(this.exporter, RecipeConstants.PINK_DYE_RECIPE_KEY);
    }

}
//?}