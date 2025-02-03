package com.axialeaa.florumsporum.data.provider;

import com.axialeaa.florumsporum.util.Identifiers;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

//? if 1.20.1 {
/*import net.minecraft.data.server.recipe.RecipeJsonProvider;
import java.util.function.Consumer;
*///?} elif <1.21.4 {
/*import net.minecraft.data.server.recipe.RecipeExporter;
*///?} else {
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
//?}

//? if >=1.20.6 {
import net.minecraft.registry.RegistryWrapper;
import java.util.concurrent.CompletableFuture;
//?}

//? if >=1.21.3 {
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
//?}

//? if <1.21.3 {
/*import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
*///?} elif 1.21.3
/*import net.minecraft.data.server.recipe.RecipeGenerator;*/

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
        return new RecipeGenerator(registries, recipeExporter) {

            @Override
            public void generate() {
                this.createShapeless(RecipeCategory.MISC, Items.PINK_DYE)
                    .input(Items.SPORE_BLOSSOM)
                    .group("pink_dye")
                    .criterion(hasItem(Items.SPORE_BLOSSOM), this.conditionsFromItem(Items.SPORE_BLOSSOM))
                    .offerTo(this.exporter, RegistryKey.of(RegistryKeys.RECIPE, Identifiers.PINK_DYE_FROM_SPORE_BLOSSOM_RECIPE));
            }

        };
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
            .criterion(hasItem(Items.SPORE_BLOSSOM), conditionsFromItem(Items.SPORE_BLOSSOM))
            .group("pink_dye")
            .offerTo(exporter, Identifiers.PINK_DYE_FROM_SPORE_BLOSSOM_RECIPE);
    }
    *///?}

    @Override
    public String getName() {
        return "Recipe Provider";
    }

}
