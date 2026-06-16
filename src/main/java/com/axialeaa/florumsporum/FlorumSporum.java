package com.axialeaa.florumsporum;

import com.axialeaa.florumsporum.data.registry.FlorumSporumGameRules;
import com.axialeaa.florumsporum.data.registry.FlorumSporumSoundEvents;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
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
        FlorumSporumGameRules.init();
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static <T> ResourceKey<T> resourceKey(ResourceKey<? extends Registry<T>> registryKey, String path) {
        return ResourceKey.create(registryKey, id(path));
    }

}
