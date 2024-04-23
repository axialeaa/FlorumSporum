package com.axialeaa.florumsporum.util;

import com.axialeaa.florumsporum.mixin.SporeBlossomBlockMixin;
import com.axialeaa.florumsporum.registry.FlorumSporumSoundEvents;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

import java.util.Map;

/*? if >1.18.2 {*/
import net.minecraft.util.math.random.Random;
/*?} else {*//*
import java.util.Random;
*//*?} */

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
     * A blockstate property that specifies whether the spore blossom is closed.
     */
    public static final EnumProperty<Openness> OPENNESS = EnumProperty.of("openness", Openness.class);

    private static final Map<Direction, VoxelShape> SHAPES_FOR_DIRECTION = Util.make(Maps.newHashMap(), map -> {
        map.put(Direction.DOWN, Block.createCuboidShape(2.0, 13.0, 2.0, 14.0, 16.0, 14.0));
        map.put(Direction.UP, Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 3.0, 14.0));
        map.put(Direction.NORTH, Block.createCuboidShape(2.0, 2.0, 13.0, 14.0, 14.0, 16.0));
        map.put(Direction.EAST, Block.createCuboidShape(0.0, 2.0, 2.0, 3.0, 14.0, 14.0));
        map.put(Direction.SOUTH, Block.createCuboidShape(2.0, 2.0, 0.0, 14.0, 14.0, 3.0));
        map.put(Direction.WEST,  Block.createCuboidShape(13.0, 2.0, 2.0, 16.0, 14.0, 14.0));
    });

    public static VoxelShape getShapeForDirection(Direction direction) {
        return SHAPES_FOR_DIRECTION.get(direction);
    }

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
     * @return the openness property of the spore blossom passed through {@code state}.
     */
    public static Openness getOpenness(BlockState state) {
        return state.get(OPENNESS);
    }

    /**
     * @return true if the spore blossom (passed through {@code state}) is fully closed.
     */
    public static boolean isFullyClosed(BlockState state) {
        return getOpenness(state) == Openness.CLOSED;
    }

    /**
     * @return true if the spore blossom (passed through {@code state}) is fully open.
     */
    public static boolean isFullyOpen(BlockState state) {
        if (getAge(state) == 0)
            return false;

        return getOpenness(state).ordinal() == getAge(state);
    }

    /**
     * Fully closes the spore blossom (if the current state supports it) and plays a sound.
     * @return true if the closure was successful.
     * @implNote The return value is used for conditionally scheduling a block tick when this method succeeds, instead of boilerplating the {@link SporeBlossomStatics#isFullyClosed(BlockState)} call.
     * @see SporeBlossomBlockMixin#onEntityCollision(BlockState, World, BlockPos, Entity)
     */
    public static boolean closeFully(World world, BlockPos pos, BlockState state) {
        if (isFullyClosed(state))
            return false;

        playSound(world, pos, FlorumSporumSoundEvents.SPORE_BLOSSOM_CLOSE);
        world.setBlockState(pos, state.with(OPENNESS, Openness.CLOSED));

        return true;
    }

    /**
     * Opens the spore blossom to the maximum stage permitted by the spore blossom's age and plays a sound.
     * @return true if the opening was successful.
     * @implNote Here, the return value isn't actually used. I've just kept it in for readability reasons.
     * @see SporeBlossomBlockMixin#neighborUpdate(BlockState, World, BlockPos, Block, BlockPos, boolean)
     */
    public static boolean openFully(World world, BlockPos pos, BlockState state) {
        if (isFullyOpen(state) || getAge(state) == 0)
            return false;

        playSound(world, pos, FlorumSporumSoundEvents.SPORE_BLOSSOM_OPEN);
        world.setBlockState(pos, state.with(OPENNESS, Openness.get(getAge(state))));

        return true;
    }

    /**
     * Opens the spore blossom to the next stage and plays a sound.
     * @return true if the opening was successful.
     * @implNote The return value is used for conditionally scheduling a block tick when this method succeeds, instead of boilerplating the {@link SporeBlossomStatics#isFullyOpen(BlockState)} call.
     * @see SporeBlossomBlockMixin#scheduledTick(BlockState, ServerWorld, BlockPos, Random)
     */
    public static boolean openNext(World world, BlockPos pos, BlockState state) {
        if (isFullyOpen(state) || getAge(state) == 0)
            return false;

        playSound(world, pos, FlorumSporumSoundEvents.SPORE_BLOSSOM_OPEN);
        world.setBlockState(pos, state.with(OPENNESS, Openness.get(Math.min(getOpenness(state).ordinal() + 1, 3))));

        return true;
    }

    /**
     * @return true if there is at least one non-spectator entity located at {@code pos}.
     */
    public static boolean hasEntityAt(World world, BlockPos pos) {
        return !world.getEntitiesByClass(LivingEntity.class, new Box(pos), EntityPredicates.EXCEPT_SPECTATOR).isEmpty();
    }

    /**
     * @return the block position the spore blossom (passed through {@code state}) is resting on.
     */
    public static BlockPos getSupportingPos(BlockPos pos, BlockState state) {
        return pos.offset(getFacing(state).getOpposite());
    }

    /**
     * Increments the spore blossom's age and openness value and plays a sound.
     * @return true if the increment was successful.
     * @implNote The return value is used for conditionally dropping an item when this method fails, instead of boilerplating the {@link SporeBlossomStatics#isMaxAge(BlockState)} call.
     * @see SporeBlossomBlockMixin#grow(ServerWorld, Random, BlockPos, BlockState)
     * @see SporeBlossomBlockMixin#randomTick(BlockState, ServerWorld, BlockPos, Random)
     */
    public static boolean advanceAge(ServerWorld world, BlockPos pos, BlockState state) {
        if (isMaxAge(state))
            return false;

        int i = getAge(state) + 1;
        world.setBlockState(pos, state.with(AGE, i).with(OPENNESS, Openness.get(i)), Block.NOTIFY_LISTENERS);

        return true;
    }

    /**
     * @return true if the spore blossom (passed through {@code state}) is the maximum age it can reach.
     */
    public static boolean isMaxAge(BlockState state) {
        return getAge(state) == 3;
    }

    /**
     * Plays a sound at {@code pos}.
     */
    public static void playSound(World world, BlockPos pos, SoundEvent soundEvent) {
        world.playSound(null, pos, soundEvent, SoundCategory.BLOCKS, 1.0F, MathHelper.nextBetween(world.random, 0.8F, 1.2F));
    }

}