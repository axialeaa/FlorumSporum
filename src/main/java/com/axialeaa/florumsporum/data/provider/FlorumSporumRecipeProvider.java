package com.axialeaa.florumsporum.data.provider;

import com.axialeaa.florumsporum.FlorumSporum;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public class FlorumSporumRecipeProvider extends FabricRecipeProvider {

    public FlorumSporumRecipeProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> holderProviderFuture) {
        super(output, holderProviderFuture);
    }

	@Override
	protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, BootstrapContext<Recipe<?>> recipes, BootstrapContext<Advancement> advancements) {
		return new Runner(recipes, advancements);
	}

    private static class Runner extends RecipeProvider {

		private Runner(BootstrapContext<Recipe<?>> recipeOutput, BootstrapContext<Advancement> advancementOutput) {
			super(recipeOutput, advancementOutput);
		}

	    @Override
        public void buildRecipes() {
			this.save("pink_dye_from_spore_blossom", this.shapeless(RecipeCategory.MISC, Items.DYE.pink())
                .requires(Items.SPORE_BLOSSOM)
                .group("pink_dye")
                .unlockedBy(getHasName(Items.SPORE_BLOSSOM), this.has(Items.SPORE_BLOSSOM))
			);
        }

		private void save(String path, RecipeBuilder builder) {
			builder.save(this.output, FlorumSporum.id(path).toString());
		}

    }

}