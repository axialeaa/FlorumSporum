package com.axialeaa.florumsporum.item;

import com.axialeaa.florumsporum.block.SporeBlossomBehaviour;
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

        for (int i = 0; i <= SporeBlossomBehaviour.MAX_AGE; i++) {
            if (i == 0) {
                prevStack = SporeBlossomStack.addDataForAge(stack, 0); // replacing the vanilla spore blossom stack with one of age 0
                continue;
            }

            ItemStack sporeBlossomStack = SporeBlossomStack.create(i);
            entries.addAfter(prevStack, sporeBlossomStack);

            prevStack = sporeBlossomStack;
        }

        return true;
    }

}
