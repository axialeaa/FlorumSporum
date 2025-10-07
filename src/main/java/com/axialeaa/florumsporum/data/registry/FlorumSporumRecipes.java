package com.axialeaa.florumsporum.data.registry;

import com.axialeaa.florumsporum.FlorumSporum;
import com.axialeaa.florumsporum.data.FlorumSporumRecipeGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public interface FlorumSporumRecipes {

    RegistryKey<Recipe<?>> PINK_DYE_RECIPE_KEY = RegistryKey.of(RegistryKeys.RECIPE, FlorumSporum.id("pink_dye_from_spore_blossom"));

    static void load() {}

    class Provider extends FabricRecipeProvider {

        public Provider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter recipeExporter) {
            return new FlorumSporumRecipeGenerator(registries, recipeExporter);
        }

        @Override
        public String getName() {
            return "Recipe Provider";
        }

    }

}
