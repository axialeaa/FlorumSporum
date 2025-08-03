package com.axialeaa.florumsporum.registry;

import com.axialeaa.florumsporum.FlorumSporum;
import net.minecraft.block.Block;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class FlorumSporumTags {

    public static final TagKey<Block> SPORE_BLOSSOM_CAN_GROW_ON = of(RegistryKeys.BLOCK, "spore_blossom_can_grow_on");

    private static <T> TagKey<T> of(RegistryKey<Registry<T>> registryKey, String path) {
        return TagKey.of(registryKey, FlorumSporum.id(path));
    }

    public static void init() {}

}
