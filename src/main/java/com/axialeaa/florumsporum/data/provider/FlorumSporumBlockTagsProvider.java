package com.axialeaa.florumsporum.data.provider;

import com.axialeaa.florumsporum.data.registry.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.references.BlockItemIds;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public class FlorumSporumBlockTagsProvider extends FabricTagsProvider.BlockTagsProvider {

    public FlorumSporumBlockTagsProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> holderProviderFuture) {
        super(output, holderProviderFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider holderProvider) {
        this.builder(ModTags.Blocks.SPORE_BLOSSOM_CAN_GROW_ON).add(BlockItemIds.MOSS_BLOCK);
    }

}