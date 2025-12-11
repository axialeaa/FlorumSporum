package com.axialeaa.florumsporum.item;

import com.axialeaa.florumsporum.block.property.SporeBlossomProperties;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ItemGroupModifications {

    public static void registerSporeBlossomAges(CreativeModeTab tab, FabricItemGroupEntries entries) {
        if (!tab.contains(Items.SPORE_BLOSSOM.getDefaultInstance()))
            return;

        for (ItemStack displayStack : tab.getDisplayItems()) {
            if (tryAddSporeBlossomStacks(displayStack, entries))
                return;
        }
    }

    private static boolean tryAddSporeBlossomStacks(ItemStack displayStack, FabricItemGroupEntries entries) {
        if (!displayStack.is(Items.SPORE_BLOSSOM))
            return false;

        ItemStack prevStack = SporeBlossomStack.addDataForAge(displayStack, 0); // replacing the vanilla spore blossom stack

        for (int i = 1; i < SporeBlossomProperties.GROWTH_STAGE_COUNT; i++) {
            ItemStack sporeBlossomStack = SporeBlossomStack.create(i);
            entries.addAfter(prevStack, sporeBlossomStack);

            prevStack = sporeBlossomStack;
        }

        return true;
    }

}
