package com.axialeaa.florumsporum.util;

import com.axialeaa.florumsporum.mixin.SporeBlossomBlockMixin;
import com.axialeaa.florumsporum.registry.FlorumSporumSoundEvents;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;

/**
 * The purpose of this class is to store static fields and methods used by {@link SporeBlossomBlockMixin SporeBlossomBlockMixin} without needing to make them private. This allows them to be called outside of that mixin.
 */
public class SporeBlossomUtils {

    public static final IntProperty AGE = Properties.AGE_3;
    public static final DirectionProperty FACING = Properties.FACING;
    public static final EnumProperty<Openness> OPENNESS = EnumProperty.of("openness", Openness.class);

    public static final Predicate<Entity> CAUSES_RECOIL = entity -> entity instanceof LivingEntity && entity.isAlive() && !entity.isSpectator();

    private static final ImmutableMap<Direction, VoxelShape> SHAPES = ImmutableMap.of(
        Direction.DOWN,  Block.createCuboidShape(2.0, 13.0, 2.0, 14.0, 16.0, 14.0),
        Direction.UP,    Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 3.0, 14.0),
        Direction.NORTH, Block.createCuboidShape(2.0, 2.0, 13.0, 14.0, 14.0, 16.0),
        Direction.SOUTH, Block.createCuboidShape(2.0, 2.0, 0.0, 14.0, 14.0, 3.0),
        Direction.WEST,  Block.createCuboidShape(13.0, 2.0, 2.0, 16.0, 14.0, 14.0),
        Direction.EAST,  Block.createCuboidShape(0.0, 2.0, 2.0, 3.0, 14.0, 14.0)
    );

    public static VoxelShape getShapeByDirection(Direction direction) {
        return SHAPES.get(direction);
    }

    public static int getAge(BlockState state) {
        return state.get(AGE);
    }

    public static Direction getFacing(BlockState state) {
        return state.get(FACING);
    }

    public static Openness getOpenness(BlockState state) {
        return state.get(OPENNESS);
    }

    public static boolean isFullyGrown(BlockState state) {
        return getAge(state) == 3;
    }

    public static boolean isFullyClosed(BlockState state) {
        return getOpenness(state) == Openness.CLOSED;
    }

    public static boolean isFullyOpen(BlockState state) {
        return getOpenness(state).ordinal() == getAge(state);
    }

    public static boolean isInvalid(BlockState state) {
        return getOpenness(state).ordinal() > getAge(state);
    }

    public static BlockState advanceAge(BlockState state) {
        BlockState blockState = state.cycle(AGE);
        Openness newOpenness = Openness.byOrdinal(getAge(blockState));

        return blockState.with(OPENNESS, newOpenness);
    }

    public static BlockPos getSupportingPos(BlockPos pos, BlockState state) {
        Direction facing = getFacing(state);
        return pos.offset(facing.getOpposite());
    }

    public static BlockState close(BlockState state) {
        return state.with(OPENNESS, Openness.CLOSED);
    }

    public static BlockState openFully(BlockState state) {
        Openness newOpenness = Openness.byOrdinal(getAge(state));
        return state.with(OPENNESS, newOpenness);
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