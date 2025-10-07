package com.axialeaa.florumsporum.item;

import com.axialeaa.florumsporum.block.property.SporeBlossomProperties;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ItemGroupModifications {

    public static void registerSporeBlossomAges(ItemGroup group, FabricItemGroupEntries entries) {
        if (!group.contains(Items.SPORE_BLOSSOM.getDefaultStack()))
            return;

        for (ItemStack displayStack : group.getDisplayStacks()) {
            if (tryAddSporeBlossomStacks(displayStack, entries))
                return;
        }
    }

    private static boolean tryAddSporeBlossomStacks(ItemStack displayStack, FabricItemGroupEntries entries) {
        if (!displayStack.isOf(Items.SPORE_BLOSSOM))
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
