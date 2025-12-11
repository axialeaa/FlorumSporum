package com.axialeaa.florumsporum.block.property;


import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class SporeBlossomProperties {

    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    public static final EnumProperty<Openness> OPENNESS = EnumProperty.create("openness", Openness.class);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    public static final int MAX_AGE = 3;
    public static final int GROWTH_STAGE_COUNT = MAX_AGE + 1;

    public static int getAge(BlockState state) {
        return state.getValue(AGE);
    }

    public static Direction getFacing(BlockState state) {
        return state.getValue(FACING);
    }

    public static Openness getOpenness(BlockState state) {
        return state.getValue(OPENNESS);
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
