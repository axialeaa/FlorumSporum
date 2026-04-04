package com.axialeaa.florumsporum.data.registry;

import com.axialeaa.florumsporum.FlorumSporum;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public interface FlorumSporumBlockTags {

    TagKey<Block> SPORE_BLOSSOM_CAN_GROW_ON = TagKey.create(Registries.BLOCK, FlorumSporum.id("spore_blossom_can_grow_on"));

    static void init() {}

}
