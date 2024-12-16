package com.axialeaa.florumsporum;

import com.axialeaa.florumsporum.registry.FlorumSporumSoundEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlorumSporum implements ModInitializer {

    public static final String MOD_ID = "florum-sporum";

    public static final FabricLoader LOADER = FabricLoader.getInstance();
    public static final ModContainer CONTAINER = LOADER.getModContainer(MOD_ID).orElseThrow(RuntimeException::new);

    public static final String MOD_NAME = CONTAINER.getMetadata().getName();
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onInitialize() {
        FlorumSporumSoundEvents.init();
    }

    public static Identifier id(String path) {
        return /*$ identifier*/ Identifier.of(MOD_ID, path);
    }

}
