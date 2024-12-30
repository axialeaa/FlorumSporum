package com.axialeaa.florumsporum.registry;

import com.axialeaa.florumsporum.FlorumSporum;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class FlorumSporumSoundEvents {

    public static final SoundEvent SPORE_BLOSSOM_CLOSE = of("block.spore_blossom.close");
    public static final SoundEvent SPORE_BLOSSOM_OPEN = of("block.spore_blossom.open");

    private static SoundEvent of(String path) {
        Identifier id = FlorumSporum.id(path);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void init() {
        FlorumSporum.logRegistryInit("sound events");
    }

}
