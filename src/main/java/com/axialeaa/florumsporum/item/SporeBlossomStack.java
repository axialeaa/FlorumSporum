package com.axialeaa.florumsporum.item;

import com.axialeaa.florumsporum.block.property.SporeBlossomProperties;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SporeBlossomStack {

    public static ItemStack create(BlockState state) {
        return create(SporeBlossomProperties.getAge(state));
    }

    public static ItemStack create(int age) {
        ItemStack stack = Items.SPORE_BLOSSOM.getDefaultStack();
        return age == 3 ? stack : addDataForAge(stack, age);
    }

    public static void dropJuvenile(World world, BlockPos pos) {
        Block.dropStack(world, pos, create(0));
    }

    public static ItemStack addDataForAge(ItemStack stack, int age) {
        stack.set(
            DataComponentTypes.BLOCK_STATE,
            BlockStateComponent.DEFAULT.with(SporeBlossomProperties.AGE, age)
        );

        return stack;
    }

    public static int getAgeComponentValue(ItemStack stack) {
        int age = SporeBlossomProperties.MAX_AGE;

        BlockStateComponent blockStateComponent = stack.getOrDefault(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT);
        Integer componentValue = blockStateComponent.getValue(SporeBlossomProperties.AGE);

        if (componentValue != null)
            age = componentValue;

        return age;
    }

    public static Text getTooltip(ItemStack stack) {
        int growthStage = getAgeComponentValue(stack) + 1;
        return Text.translatable("block.spore_blossom.growth_stage", growthStage).formatted(Formatting.GRAY);
    }

}
