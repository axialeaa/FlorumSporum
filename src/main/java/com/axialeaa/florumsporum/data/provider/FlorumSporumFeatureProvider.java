package com.axialeaa.florumsporum.data.provider;

import com.axialeaa.florumsporum.block.SporeBlossomBehaviour;
import com.axialeaa.florumsporum.block.property.SporeBlossomProperties;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.worldgen.features.CaveFeatures;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;

import java.util.concurrent.CompletableFuture;

public class FlorumSporumFeatureProvider extends FabricDynamicRegistryProvider {

	public FlorumSporumFeatureProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
		super(output, registriesFuture);
	}

	@Override
	protected void configure(HolderLookup.Provider registries, Entries entries) {
		WeightedList.Builder<BlockState> builder = WeightedList.builder();

		for (int i = 0; i <= SporeBlossomProperties.MAX_AGE; i++) {
			BlockState blockState = Blocks.SPORE_BLOSSOM.defaultBlockState().setValue(SporeBlossomProperties.AGE, i);
			builder.add(SporeBlossomBehaviour.open(blockState), i == 0 ? 3 : 1);
		}

		entries.add(CaveFeatures.SPORE_BLOSSOM, new SimpleBlockFeature(new WeightedStateProvider(builder.build())));
	}

	@Override
	public String getName() {
		return "Features";
	}

}
