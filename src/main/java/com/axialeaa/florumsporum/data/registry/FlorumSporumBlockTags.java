package com.axialeaa.florumsporum.data.registry;

import com.axialeaa.florumsporum.FlorumSporum;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public interface FlorumSporumBlockTags {

    TagKey<Block> SPORE_BLOSSOM_CAN_GROW_ON = TagKey.create(Registries.BLOCK, FlorumSporum.id("spore_blossom_can_grow_on"));

    static void load() {}

    class Provider extends FabricTagProvider.BlockTagProvider {

        public Provider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> holderProviderFuture) {
            super(output, holderProviderFuture);
        }

        @Override
        protected void addTags(HolderLookup.Provider holderProvider) {
            this.valueLookupBuilder(SPORE_BLOSSOM_CAN_GROW_ON).add(Blocks.MOSS_BLOCK);
        }

    }

}
