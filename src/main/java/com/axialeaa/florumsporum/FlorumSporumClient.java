package com.axialeaa.florumsporum;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.minecraft.util.Identifier;

import net.minecraft.text.Text;
import java.util.function.Function;

public class FlorumSporumClient implements ClientModInitializer {

    private static final Function<String, Text> translation = Text::translatable;

    @Override
    public void onInitializeClient() {
        registerPack("32x_upscale");
    }

    private static void registerPack(String path) {
        registerPack(FlorumSporum.id(path));
    }

    private static void registerPack(Identifier id) {
        Text translated = translation.apply("resourcePack.%s.name".formatted(id));
        ResourceManagerHelper.registerBuiltinResourcePack(id, FlorumSporum.CONTAINER, translated, ResourcePackActivationType.NORMAL);

        FlorumSporum.LOGGER.info("Registered pack {}!", id);
    }

}