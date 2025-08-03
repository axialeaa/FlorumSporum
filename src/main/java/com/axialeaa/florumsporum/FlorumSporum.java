package com.axialeaa.florumsporum;

import com.axialeaa.florumsporum.item.ItemGroupModifications;
import com.axialeaa.florumsporum.item.LootTableModifications;
import com.axialeaa.florumsporum.registry.FlorumSporumTags;
import com.axialeaa.florumsporum.registry.FlorumSporumSoundEvents;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlorumSporum implements ModInitializer {

    public static final String MOD_ID = "florum-sporum";
    public static final String MOD_NAME = "Florum Sporum";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onInitialize() {
        FlorumSporumSoundEvents.init();
        FlorumSporumTags.init();
        ItemGroupModifications.addSporeBlossomAges();
        LootTableModifications.register();
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

}
