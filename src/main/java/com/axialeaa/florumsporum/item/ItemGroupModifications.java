package com.axialeaa.florumsporum.item;

import com.axialeaa.florumsporum.block.property.SporeBlossomProperties;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ItemGroupModifications {

    public static void addSporeBlossomAges() {
        ItemGroupEvents.MODIFY_ENTRIES_ALL.register((group, entries) -> {
            if (!group.contains(Items.SPORE_BLOSSOM.getDefaultStack()))
                return;

            for (ItemStack itemStack : group.getDisplayStacks()) {
                if (tryAddSporeBlossomStacks(itemStack, entries))
                    return;
            }
        });
    }

    private static boolean tryAddSporeBlossomStacks(ItemStack stack, FabricItemGroupEntries entries) {
        if (!stack.isOf(Items.SPORE_BLOSSOM))
            return false;

        ItemStack prevStack = null;

        for (int i = 0; i < SporeBlossomProperties.GROWTH_STAGE_COUNT; i++) {
            if (i == 0) {
                prevStack = SporeBlossomStack.addDataForAge(stack, 0); // replacing the vanilla spore blossom stack
                continue;
            }

            ItemStack sporeBlossomStack = SporeBlossomStack.create(i);
            entries.addAfter(prevStack, sporeBlossomStack);

            prevStack = sporeBlossomStack;
        }

        return true;
    }

}
