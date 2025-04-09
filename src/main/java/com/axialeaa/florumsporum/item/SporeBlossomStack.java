package com.axialeaa.florumsporum.item;

import com.axialeaa.florumsporum.block.property.SporeBlossomProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

//? if >=1.20.6 {
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
//?} else
/*import net.minecraft.nbt.NbtCompound;*/

public class SporeBlossomStack {

    private static final String TOOLTIP_KEY = "block.spore_blossom.growth_stage";

    //? if <1.21.4
    /*public static final String AGE_NAME = SporeBlossomProperties.AGE.getName();*/

    //? if <1.20.6
    /*public static final String BLOCK_STATE_TAG = "BlockStateTag";*/

    public static ItemStack create(int age) {
        return addDataForAge(Items.SPORE_BLOSSOM.getDefaultStack(), age);
    }

    public static ItemStack addDataForAge(ItemStack stack, int age) {
        //? if >=1.20.6 {
        stack.set(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT.with(SporeBlossomProperties.AGE, age));
        //?} else {
        /*NbtCompound nbtCompound = new NbtCompound();
        nbtCompound.putString(AGE_NAME, String.valueOf(age));

        stack.setSubNbt(BLOCK_STATE_TAG, nbtCompound);
        *///?}

        return stack;
    }

    public static int getAgeComponentValue(ItemStack stack) {
        int age = SporeBlossomProperties.MAX_AGE;

        //? if >=1.20.6 {
        BlockStateComponent blockStateComponent = stack.getOrDefault(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT);
        var componentValue = blockStateComponent.getValue(SporeBlossomProperties.AGE);

        if (componentValue != null)
            age = componentValue;
        //?} else {
        /*NbtCompound compound = stack.getSubNbt(BLOCK_STATE_TAG);

        try {
            if (compound != null && compound.contains(AGE_NAME))
                age = Integer.parseInt(compound.getString(AGE_NAME));
        }
        catch (NumberFormatException ignored) {}
        *///?}

        return age;
    }

    public static void addTooltip(List<Text> list, ItemStack stack) {
        if (stack.isOf(Items.SPORE_BLOSSOM))
            list.add(getTooltip(stack));
    }

    public static Text getTooltip(ItemStack stack) {
        int growthStage = getAgeComponentValue(stack) + 1;
        return Text.translatable(TOOLTIP_KEY, growthStage).formatted(Formatting.GRAY);
    }

}
