package com.axialeaa.florumsporum;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class FlorumSporumClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        registerPack("32x_upscale");
    }

    private static Text translate(String key) {
        return /*$ translatable*/ Text.translatable(key);
    }

    private static void registerPack(String name) {
        Identifier id = FlorumSporum.id(name);
        Text translated = translate("resourcePack.%s.name".formatted(id));

        ResourceManagerHelper.registerBuiltinResourcePack(id, FlorumSporum.CONTAINER,
            //? if >1.17.1
            /*$ pack_name >>*/ translated ,
            ResourcePackActivationType.NORMAL);

        FlorumSporum.LOGGER.info("Registered pack {}!", id);
    }

}
