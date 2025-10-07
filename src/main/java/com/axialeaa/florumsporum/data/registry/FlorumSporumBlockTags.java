package com.axialeaa.florumsporum.data.registry;

import com.axialeaa.florumsporum.FlorumSporum;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;

import java.util.concurrent.CompletableFuture;

public interface FlorumSporumBlockTags {

    TagKey<Block> SPORE_BLOSSOM_CAN_GROW_ON = TagKey.of(RegistryKeys.BLOCK, FlorumSporum.id("spore_blossom_can_grow_on"));

    static void load() {}

    class Provider extends FabricTagProvider.BlockTagProvider {

        public Provider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup registries) {
            this.valueLookupBuilder(SPORE_BLOSSOM_CAN_GROW_ON).add(Blocks.MOSS_BLOCK);
        }

    }

}
