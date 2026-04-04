package com.axialeaa.florumsporum.data.provider;

import com.axialeaa.florumsporum.data.registry.FlorumSporumRecipes;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public class FlorumSporumRecipeProvider extends FabricRecipeProvider {

    public FlorumSporumRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> holderProviderFuture) {
        super(output, holderProviderFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider holderProvider, RecipeOutput output) {
        return new Runner(holderProvider, output);
    }

    @Override
    public String getName() {
        return "Recipe Provider";
    }

    private static class Runner extends RecipeProvider {

        private Runner(HolderLookup.Provider holderProvider, RecipeOutput output) {
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

}