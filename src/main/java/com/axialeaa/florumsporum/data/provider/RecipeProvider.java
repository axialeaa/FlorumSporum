package com.axialeaa.florumsporum.data.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;

//? if 1.20.1
/*import java.util.function.Consumer;*/

//? if <1.21.4 {
/*import net.minecraft.data.server.recipe.*;
*///?} else
import net.minecraft.data.recipe.*;

//? if >=1.20.6 {
import net.minecraft.registry.RegistryWrapper;
import java.util.concurrent.CompletableFuture;
//?}

//? if >=1.21.3 {
import com.axialeaa.florumsporum.data.recipe.FlorumSporumRecipeGenerator;
//?} else {
/*import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import com.axialeaa.florumsporum.data.recipe.RecipeConstants;
*///?}

public class RecipeProvider extends FabricRecipeProvider {

    //? if >=1.20.6 {
    public RecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }
    //?} else {
    /*public RecipeProvider(FabricDataOutput output) {
        super(output);
    }
    *///?}

    //? if >=1.21.3 {
    @Override
    protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter recipeExporter) {
        return new FlorumSporumRecipeGenerator(registries, recipeExporter);
    }
    //?} else {
    /*@Override
    public void generate(
        //? if 1.20.1 {
        /^Consumer<RecipeJsonProvider> exporter
        ^///?} else
        RecipeExporter exporter
    ) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.PINK_DYE)
            .input(Items.SPORE_BLOSSOM)
            .criterion(RecipeConstants.HAS_SPORE_BLOSSOM, conditionsFromItem(Items.SPORE_BLOSSOM))
            .group(RecipeConstants.PINK_DYE)
            .offerTo(exporter, RecipeConstants.PINK_DYE_ID);
    }
    *///?}

    @Override
    public String getName() {
        return "Recipe Provider";
    }

}
