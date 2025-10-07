package com.axialeaa.florumsporum;

import com.axialeaa.florumsporum.item.ItemGroupModifications;
import com.axialeaa.florumsporum.item.LootTableModifications;
import com.axialeaa.florumsporum.data.registry.FlorumSporumBlockTags;
import com.axialeaa.florumsporum.data.registry.FlorumSporumRecipes;
import com.axialeaa.florumsporum.data.registry.FlorumSporumSoundEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlorumSporum implements ModInitializer {

    public static final String MOD_ID = "florum-sporum";
    public static final String MOD_NAME = "Florum Sporum";

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onInitialize() {
        LOGGER.info("{} initialized! Have some florum decorum...", MOD_NAME);

        FlorumSporumSoundEvents.load();
        FlorumSporumBlockTags.load();
        FlorumSporumRecipes.load();

        LootTableModifications.register();
        ItemGroupEvents.MODIFY_ENTRIES_ALL.register(ItemGroupModifications::registerSporeBlossomAges);
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

}
