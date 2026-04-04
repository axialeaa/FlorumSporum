package com.axialeaa.florumsporum;

import com.axialeaa.florumsporum.data.registry.FlorumSporumGameRules;
import com.axialeaa.florumsporum.data.registry.FlorumSporumBlockTags;
import com.axialeaa.florumsporum.data.registry.FlorumSporumRecipes;
import com.axialeaa.florumsporum.data.registry.FlorumSporumSoundEvents;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlorumSporum implements ModInitializer {

    public static final String MOD_ID = "florum-sporum";
    public static final String MOD_NAME = "Florum Sporum";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onInitialize() {
        LOGGER.info("{} initialized! Have some florum decorum...", MOD_NAME);

        FlorumSporumSoundEvents.init();
        FlorumSporumBlockTags.init();
        FlorumSporumRecipes.init();
        FlorumSporumGameRules.init();
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

}
