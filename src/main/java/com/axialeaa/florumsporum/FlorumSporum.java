package com.axialeaa.florumsporum;

import com.axialeaa.florumsporum.registry.FlorumSporumSoundEvents;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlorumSporum implements ModInitializer {

    public static final String MOD_ID = "florum-sporum";
    public static final String MOD_NAME = "Florum Sporum";
    public static Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onInitialize() {
        FlorumSporumSoundEvents.init();
    }

}
