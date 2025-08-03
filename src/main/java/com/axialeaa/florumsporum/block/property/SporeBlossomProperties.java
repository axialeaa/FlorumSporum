package com.axialeaa.florumsporum.block.property;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

public class SporeBlossomProperties {

    public static final EnumProperty<Direction> FACING = Properties.FACING;
    public static final EnumProperty<Openness> OPENNESS = EnumProperty.of("openness", Openness.class);
    public static final IntProperty AGE = Properties.AGE_3;

    public static final int MAX_AGE = 3;
    public static final int GROWTH_STAGE_COUNT = MAX_AGE + 1;

    public static int getAge(BlockState state) {
        return state.get(AGE);
    }

    public static Direction getFacing(BlockState state) {
        return state.get(FACING);
    }

    public static Openness getOpenness(BlockState state) {
        return state.get(OPENNESS);
    }

    public static boolean isMaxAge(BlockState state) {
        return getAge(state) == MAX_AGE;
    }

    public static boolean isClosed(BlockState state) {
        return getOpenness(state) == Openness.CLOSED;
    }

    public static boolean isFullyOpen(BlockState state) {
        return getOpenness(state).ordinal() == getAge(state);
    }

}
