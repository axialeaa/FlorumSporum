package com.axialeaa.florumsporum.data.registry;

import com.axialeaa.florumsporum.FlorumSporum;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

public final class FlorumSporumRecipes {

    public static final ResourceKey<Recipe<?>> PINK_DYE_RECIPE_KEY = FlorumSporum.resourceKey(Registries.RECIPE, "pink_dye_from_spore_blossom");

}
