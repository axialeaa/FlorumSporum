package com.axialeaa.florumsporum.registry;

import com.axialeaa.florumsporum.FlorumSporum;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class FlorumSporumBlockTags {

    public static final TagKey<Block> SPORE_BLOSSOM_CAN_GROW_ON = of("spore_blossom_can_grow_on");

    private static TagKey<Block> of(String path) {
        return TagKey.of(RegistryKeys.BLOCK, FlorumSporum.id(path));
    }

    public static void init() {
        FlorumSporum.logRegistryInit("block tags");
    }

}
