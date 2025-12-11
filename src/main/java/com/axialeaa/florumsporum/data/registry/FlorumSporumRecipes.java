package com.axialeaa.florumsporum.data.registry;

import com.axialeaa.florumsporum.FlorumSporum;
import com.axialeaa.florumsporum.data.FlorumSporumRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

import java.util.concurrent.CompletableFuture;

public interface FlorumSporumRecipes {

    ResourceKey<Recipe<?>> PINK_DYE_RECIPE_KEY = ResourceKey.create(Registries.RECIPE, FlorumSporum.id("pink_dye_from_spore_blossom"));

    static void load() {}

    class Provider extends FabricRecipeProvider {

        public Provider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> holderProviderFuture) {
            super(output, holderProviderFuture);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider holderProvider, RecipeOutput output) {
            return new FlorumSporumRecipeProvider(holderProvider, output);
        }

        @Override
        public String getName() {
            return "Recipe Provider";
        }

    }

}
