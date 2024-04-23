package com.axialeaa.florumsporum.registry;

import com.axialeaa.florumsporum.FlorumSporum;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

/*? if >1.19.2 { */
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
/*? } else { *//*
import net.minecraft.util.registry.Registry;
*//*? } */

public class FlorumSporumSoundEvents {

    public static SoundEvent SPORE_BLOSSOM_CLOSE = register("block.spore_blossom.close");
    public static SoundEvent SPORE_BLOSSOM_OPEN = register("block.spore_blossom.open");

    private static SoundEvent register(String id) {
        Identifier parseable = new Identifier(FlorumSporum.MOD_ID, id);
        /*? if >1.19.2 { */
        return Registry.register(Registries.SOUND_EVENT, parseable, SoundEvent.of(parseable));
        /*? } else { *//*
        return Registry.register(Registry.SOUND_EVENT, parseable, new SoundEvent(parseable));
        *//*? } */
    }

    public static void init() {
        FlorumSporum.LOGGER.info("Registered {} sound events!", FlorumSporum.MOD_NAME);
    }

}
