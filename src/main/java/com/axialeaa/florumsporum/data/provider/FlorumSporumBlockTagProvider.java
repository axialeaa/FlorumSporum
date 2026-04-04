package com.axialeaa.florumsporum.data.provider;

import com.axialeaa.florumsporum.data.registry.FlorumSporumBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public class FlorumSporumBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {

    public FlorumSporumBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> holderProviderFuture) {
        super(output, holderProviderFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider holderProvider) {
        this.valueLookupBuilder(FlorumSporumBlockTags.SPORE_BLOSSOM_CAN_GROW_ON).add(Blocks.MOSS_BLOCK);
    }

}