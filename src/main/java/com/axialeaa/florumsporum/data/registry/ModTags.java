package com.axialeaa.florumsporum.data.registry;

import com.axialeaa.florumsporum.FlorumSporum;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class ModTags {

	private ModTags() {}

	private static <T> TagKey<T> create(ResourceKey<? extends Registry<T>> registryKey, String path) {
		return TagKey.create(registryKey, FlorumSporum.id(path));
	}

	public static final class Blocks {

		public static final TagKey<Block> SPORE_BLOSSOM_CAN_GROW_ON = create("spore_blossom_can_grow_on");

		private Blocks() {}

		private static TagKey<Block> create(String path) {
			return ModTags.create(Registries.BLOCK, path);
		}

	}

}
