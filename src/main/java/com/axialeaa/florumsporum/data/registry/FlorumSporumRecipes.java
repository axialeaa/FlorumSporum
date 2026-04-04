package com.axialeaa.florumsporum.data.registry;

import com.axialeaa.florumsporum.FlorumSporum;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;

public interface FlorumSporumRecipes {

    ResourceKey<Recipe<?>> PINK_DYE_RECIPE_KEY = ResourceKey.create(Registries.RECIPE, FlorumSporum.id("pink_dye_from_spore_blossom"));

    static void init() {}

}
