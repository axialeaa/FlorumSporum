package com.axialeaa.florumsporum;

import com.axialeaa.florumsporum.registry.FlorumSporumSoundEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class FlorumSporum implements ModInitializer {

    public static final String MOD_ID = "florum-sporum";
    public static final String MOD_NAME;
    public static final Logger LOGGER;

    static {
        Optional<ModContainer> optional = FabricLoader.getInstance().getModContainer(MOD_ID);
        ModMetadata metadata = optional.orElseThrow(RuntimeException::new).getMetadata();

        MOD_NAME = metadata.getName();
        LOGGER = LoggerFactory.getLogger(MOD_NAME);
    }

    @Override
    public void onInitialize() {
        FlorumSporumSoundEvents.init();
    }

    public static Identifier id(String name) {
        return /*$ identifier*/ Identifier.of(MOD_ID, name);
    }

}
