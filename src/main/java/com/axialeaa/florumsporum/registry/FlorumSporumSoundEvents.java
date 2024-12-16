package com.axialeaa.florumsporum.registry;

import com.axialeaa.florumsporum.FlorumSporum;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class FlorumSporumSoundEvents {

    public static SoundEvent SPORE_BLOSSOM_CLOSE = register("block.spore_blossom.close");
    public static SoundEvent SPORE_BLOSSOM_OPEN = register("block.spore_blossom.open");

    private static SoundEvent register(String path) {
        Identifier id = FlorumSporum.id(path);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void init() {
        FlorumSporum.LOGGER.info("Registered {} sound events!", FlorumSporum.MOD_NAME);
    }

}
