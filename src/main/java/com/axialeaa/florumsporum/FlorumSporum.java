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

    public static final String MOD_ID = /*$ mod_id*/ "florum-sporum";
    public static final String MOD_NAME = /*$ mod_name*/ "Florum Sporum";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onInitialize() {
        FlorumSporumSoundEvents.init();
        FlorumSporumTags.init();
        ItemGroupModifications.addSporeBlossomAges();
        LootTableModifications.addSporeBlossomAgeFunction();
    }

    public static Identifier id(String path) {
        return /*$ identifier*/ Identifier.of(MOD_ID, path);
    }

    public static void logRegistryInit(String registry) {
        LOGGER.info("Registering {} {}!", MOD_NAME, registry);
    }

}
