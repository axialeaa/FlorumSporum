package com.axialeaa.florumsporum.data.provider;

import com.axialeaa.florumsporum.registry.FlorumSporumTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends FabricTagProvider.BlockTagProvider {

    public BlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries) {
        this.valueLookupBuilder(FlorumSporumTags.SPORE_BLOSSOM_CAN_GROW_ON).add(Blocks.MOSS_BLOCK);
    }

}
