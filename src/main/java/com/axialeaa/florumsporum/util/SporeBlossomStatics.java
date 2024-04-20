package com.axialeaa.florumsporum.util;

import com.axialeaa.florumsporum.mixin.SporeBlossomBlockMixin;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

/**
 * The purpose of this class is to store static fields and methods used by {@link SporeBlossomBlockMixin SporeBlossomBlockMixin} without needing to make them private. This allows them to be called outside of that mixin.
 */
public class SporeBlossomStatics {

    /**
     * A blockstate property that specifies the age of the spore blossom.
     */
    public static final IntProperty AGE = Properties.AGE_3;
    /**
     * A blockstate property that specifies the facing direction of the spore blossom.
     */
    public static final DirectionProperty FACING = Properties.FACING;
    /**
     * The maximum age the spore blossom can reach.
     */
    public static final int MAX_AGE = Properties.AGE_3_MAX;

    /**
     * @return the age value of the spore blossom passed through {@code state}.
     */
    public static int getAge(BlockState state) {
        return state.get(AGE);
    }

    /**
     * @return the facing direction of the spore blossom passed through {@code state}.
     */
    public static Direction getFacing(BlockState state) {
        return state.get(FACING);
    }

    /**
     * @return the block position the spore blossom (passed through {@code state}) is resting on.
     */
    public static BlockPos getSupportingPos(BlockPos pos, BlockState state) {
        return pos.offset(getFacing(state).getOpposite());
    }

    /**
     * Increments the age of the spore blossom (passed through {@code state}) by 1.
     */
    public static boolean advanceAge(ServerWorld world, BlockPos pos, BlockState state) {
        if (isMaxAge(state))
            return false;

        world.setBlockState(pos, state.with(AGE, getAge(state) + 1), Block.NOTIFY_LISTENERS);
        return true;
    }

    /**
     * @return true if the spore blossom (passed through {@code state}) is the maximum age it can reach.
     */
    public static boolean isMaxAge(BlockState state) {
        return getAge(state) >= MAX_AGE;
    }

}