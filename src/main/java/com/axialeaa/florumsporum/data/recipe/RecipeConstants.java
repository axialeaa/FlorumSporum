package com.axialeaa.florumsporum.data.recipe;

import com.axialeaa.florumsporum.FlorumSporum;
import net.minecraft.util.Identifier;

//? if >=1.21.3 {
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
//?}

public class RecipeConstants {

    public static final String
        HAS_SPORE_BLOSSOM = "has_spore_blossom",
        PINK_DYE = "pink_dye";

    public static final Identifier PINK_DYE_ID = FlorumSporum.id("pink_dye_from_spore_blossom");

    //? if >=1.21.3
    public static final RegistryKey<Recipe<?>> PINK_DYE_RECIPE_KEY = RegistryKey.of(RegistryKeys.RECIPE, PINK_DYE_ID);

}