package com.axialeaa.florumsporum.data.registry;

import com.axialeaa.florumsporum.FlorumSporum;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public final class FlorumSporumSoundEvents {

    public static final SoundEvent SPORE_BLOSSOM_CLOSE = of("block.spore_blossom.close");
    public static final SoundEvent SPORE_BLOSSOM_OPEN = of("block.spore_blossom.open");

    public static SoundEvent of(String path) {
        Identifier id = FlorumSporum.id(path);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void init() {}

}
