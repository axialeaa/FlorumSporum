package com.axialeaa.florumsporum;

import com.axialeaa.florumsporum.item.SporeBlossomStack;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.world.item.Items;

public class FlorumSporumClient implements ClientModInitializer {

    private static final int COMPONENT_TOOLTIP_LINE_INDEX = 1; // this puts it beneath the name but above the greyed-out component data and creative tab reference.

    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, lines) -> {
            if (stack.is(Items.SPORE_BLOSSOM))
                lines.add(COMPONENT_TOOLTIP_LINE_INDEX, SporeBlossomStack.getTooltip(stack));
        });
    }

}