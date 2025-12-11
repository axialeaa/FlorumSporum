package com.axialeaa.florumsporum.item;

import com.axialeaa.florumsporum.block.property.SporeBlossomProperties;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class SporeBlossomStack {

    public static ItemStack create(BlockState state) {
        return create(SporeBlossomProperties.getAge(state));
    }

    public static ItemStack create(int age) {
        ItemStack stack = Items.SPORE_BLOSSOM.getDefaultInstance();
        return age == 3 ? stack : addDataForAge(stack, age);
    }

    public static void dropJuvenile(Level level, BlockPos pos) {
        Block.popResource(level, pos, create(0));
    }

    public static ItemStack addDataForAge(ItemStack stack, int age) {
        stack.set(
            DataComponents.BLOCK_STATE,
            BlockItemStateProperties.EMPTY.with(SporeBlossomProperties.AGE, age)
        );

        return stack;
    }

    public static int getAgeComponentValue(ItemStack stack) {
        int age = SporeBlossomProperties.MAX_AGE;

        BlockItemStateProperties component = stack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
        Integer componentValue = component.get(SporeBlossomProperties.AGE);

        if (componentValue != null)
            age = componentValue;

        return age;
    }

    public static Component getTooltip(ItemStack stack) {
        int growthStage = getAgeComponentValue(stack) + 1;
        return Component.translatable("block.spore_blossom.growth_stage", growthStage).withStyle(ChatFormatting.GRAY);
    }

}
