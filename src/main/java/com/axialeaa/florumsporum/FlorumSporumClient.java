package com.axialeaa.florumsporum;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.minecraft.util.Identifier;

//? if >1.17.1 {
import net.minecraft.text.Text;
import java.util.function.Function;
//?}

//? if =1.18.2
/*import net.minecraft.text.TranslatableText;*/

public class FlorumSporumClient implements ClientModInitializer {

    //? if >1.17.1 {
    private static final Function<String, Text> translation = /*$ translatable >>*/ Text::translatable;
    //?}

    @Override
    public void onInitializeClient() {
        registerPack("32x_upscale");
    }

    private static void registerPack(String name) {
        Identifier id = FlorumSporum.id(name);

        //? if >1.17.1
        Text translated = translation.apply("resourcePack.%s.name".formatted(id));

        ResourceManagerHelper.registerBuiltinResourcePack(id, FlorumSporum.CONTAINER,
            //? if >1.17.1
            /*$ pack_name >>*/ translated,
            ResourcePackActivationType.NORMAL
        );

        FlorumSporum.LOGGER.info("Registered pack {}!", id);
    }

}