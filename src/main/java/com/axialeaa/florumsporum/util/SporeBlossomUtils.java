package com.axialeaa.florumsporum.util;

import com.axialeaa.florumsporum.mixin.SporeBlossomBlockMixin;
import com.axialeaa.florumsporum.registry.FlorumSporumSoundEvents;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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

import java.util.EnumMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * The purpose of this class is to store static fields and methods used by {@link SporeBlossomBlockMixin SporeBlossomBlockMixin} without needing to make them private. This allows them to be called outside of that mixin.
 */
public class SporeBlossomUtils {

    public static final IntProperty AGE = Properties.AGE_3;
    public static final DirectionProperty FACING = Properties.FACING;

    /**
     * A property that specifies how open a spore blossom is.
     */
    public static final EnumProperty<Openness> OPENNESS = EnumProperty.of("openness", Openness.class);

    /**
     * Tests if an entity is capable of making a spore blossom recoil.
     * <br>The entity must be living and not in spectator mode.
     * @see SporeBlossomUtils#hasEntityAt(World, BlockPos)
     */
    public static final Predicate<Entity> CAUSES_RECOIL = entity -> entity instanceof LivingEntity && entity.isAlive() && !entity.isSpectator();

    /**
     * A function that provides the spore blossom {@link MapColor} based on its facing direction.
     * <br>This outputs {@link MapColor#DARK_GREEN} when the spore blossom is facing down, or {@link MapColor#PINK} if it's facing sideways or up.
     */
    public static final Function<BlockState, MapColor> MAP_COLOR = state -> getFacing(state) == Direction.DOWN ? MapColor.DARK_GREEN : MapColor.PINK;

    /**
     * Defines the spore blossom outline shapes for each possible facing direction.
     */
    private static final EnumMap<Direction, VoxelShape> SHAPES = Util.make(Maps.newEnumMap(Direction.class), map -> {
        map.put(Direction.DOWN,  Block.createCuboidShape(2.0, 13.0, 2.0, 14.0, 16.0, 14.0));
        map.put(Direction.UP,    Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 3.0, 14.0));
        map.put(Direction.NORTH, Block.createCuboidShape(2.0, 2.0, 13.0, 14.0, 14.0, 16.0));
        map.put(Direction.SOUTH, Block.createCuboidShape(2.0, 2.0, 0.0, 14.0, 14.0, 3.0));
        map.put(Direction.WEST,  Block.createCuboidShape(13.0, 2.0, 2.0, 16.0, 14.0, 14.0));
        map.put(Direction.EAST,  Block.createCuboidShape(0.0, 2.0, 2.0, 3.0, 14.0, 14.0));
    });

    public static VoxelShape getShapeByDirection(Direction direction) {
        return SHAPES.get(direction);
    }

    public static int getAge(BlockState state) {
        return state.get(AGE);
    }

    public static Direction getFacing(BlockState state) {
        return state.get(FACING);
    }

    public static Direction getSupportingDir(BlockState state) {
        return getFacing(state).getOpposite();
    }

    public static Openness getOpenness(BlockState state) {
        return state.get(OPENNESS);
    }

    public static boolean isMaxAge(BlockState state) {
        return getAge(state) == Properties.AGE_3_MAX;
    }

    public static boolean isClosed(BlockState state) {
        return getOpenness(state) == Openness.CLOSED;
    }

    public static boolean isFullyOpen(BlockState state) {
        return getOpenness(state).ordinal() == getAge(state);
    }

    public static boolean isInvalid(BlockState state) {
        return getOpenness(state).ordinal() > getAge(state);
    }

    public static BlockPos getSupportingPos(BlockPos pos, BlockState state) {
        return pos.offset(getSupportingDir(state));
    }

    public static BlockState getSupportingState(World world, BlockPos pos, BlockState state) {
        return world.getBlockState(getSupportingPos(pos, state));
    }

    public static BlockState advanceAge(BlockState state) {
        return openFully(state.cycle(AGE));
    }

    public static BlockState close(BlockState state) {
        return state.with(OPENNESS, Openness.CLOSED);
    }

    public static BlockState openFully(BlockState state) {
        int age = getAge(state);
        return state.with(OPENNESS, Openness.byOrdinal(age));
    }

    public static BlockState openNext(BlockState state) {
        return state.cycle(OPENNESS);
    }

    public static boolean hasEntityAt(World world, BlockPos pos) {
        Box box = new Box(pos);
        List<Entity> entities = world.getEntitiesByClass(Entity.class, box, CAUSES_RECOIL);

        return !entities.isEmpty();
    }

    public static void playSound(World world, BlockPos pos, boolean open) {
        SoundEvent sound = open ? FlorumSporumSoundEvents.SPORE_BLOSSOM_OPEN : FlorumSporumSoundEvents.SPORE_BLOSSOM_CLOSE;
        world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0F, MathHelper.nextBetween(world.random, 0.8F, 1.2F));
    }

}