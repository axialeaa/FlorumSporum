package com.axialeaa.florumsporum.data.registry;

import com.axialeaa.florumsporum.FlorumSporum;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public interface FlorumSporumSoundEvents {

    SoundEvent SPORE_BLOSSOM_CLOSE = of("block.spore_blossom.close");
    SoundEvent SPORE_BLOSSOM_OPEN = of("block.spore_blossom.open");

    private static SoundEvent of(String path) {
        Identifier id = FlorumSporum.id(path);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    static void init() {}

}
