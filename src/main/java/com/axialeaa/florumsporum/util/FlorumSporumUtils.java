package com.axialeaa.florumsporum.util;

import com.axialeaa.florumsporum.mixin.SporeBlossomBlockMixin;
import com.axialeaa.florumsporum.registry.FlorumSporumSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
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

//? if <=1.21.1
/*import net.minecraft.state.property.DirectionProperty;*/

/**
 * The purpose of this class is to store static fields and methods used by {@link SporeBlossomBlockMixin SporeBlossomBlockMixin} without needing to make them private. This allows them to be called outside of that mixin.
 */
public class FlorumSporumUtils {

    /**
     * The number of ticks between checking whether there is currently an entity colliding with the spore blossom.
     */
    public static final int ENTITY_CHECK_INTERVAL = 20 * 3;
    /**
     * The time between unfurling stages in ticks.
     */
    public static final int UNFURL_INTERVAL = 10;
    /**
     * The chance per random tick of the spore blossom growing one stage.
     */
    public static final float GROW_CHANCE = 0.1F;

    public static final IntProperty AGE = Properties.AGE_3;
    public static final /*$ direction_property >>*/ EnumProperty<Direction> FACING = Properties.FACING;

    /**
     * A property that specifies how open a spore blossom is.
     */
    public static final EnumProperty<Openness> OPENNESS = EnumProperty.of("openness", Openness.class);

    /**
     * Tests if an entity is capable of making a spore blossom recoil.
     * <br>The entity (if animate) must be living and not in spectator mode.
     * @see FlorumSporumUtils#hasEntityAt(World, BlockPos)
     */
    public static final Predicate<Entity> CAUSES_RECOIL = entity -> entity.isAlive() && !entity.isSpectator();

    /**
     * A function that provides the spore blossom {@link MapColor} based on its facing direction.
     * <br>This outputs {@link MapColor#DARK_GREEN} when the spore blossom is facing down, or {@link MapColor#PINK} if it's facing sideways or up.
     */
    public static final Function<BlockState, MapColor> MAP_COLOR = state -> getFacing(state) == Direction.DOWN ? MapColor.DARK_GREEN : MapColor.PINK;

    /**
     * Defines the spore blossom outline shapes for each possible facing direction.
     * @see FlorumSporumUtils#getShapeForState(BlockState)
     */
    private static final EnumMap<Direction, VoxelShape> FACING_DIR_TO_SHAPE_MAP = Util.make(new EnumMap<>(Direction.class), shapes -> {
        shapes.put(Direction.DOWN,  Block.createCuboidShape(2.0, 13.0, 2.0, 14.0, 16.0, 14.0));
        shapes.put(Direction.UP,    Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 3.0, 14.0));
        shapes.put(Direction.NORTH, Block.createCuboidShape(2.0, 2.0, 13.0, 14.0, 14.0, 16.0));
        shapes.put(Direction.SOUTH, Block.createCuboidShape(2.0, 2.0, 0.0, 14.0, 14.0, 3.0));
        shapes.put(Direction.WEST,  Block.createCuboidShape(13.0, 2.0, 2.0, 16.0, 14.0, 14.0));
        shapes.put(Direction.EAST,  Block.createCuboidShape(0.0, 2.0, 2.0, 3.0, 14.0, 14.0));
    });

    /**
     * @param state The spore blossom block state.
     * @return a {@link VoxelShape} outlining the spore blossom exclusive to its facing direction extracted from {@link FlorumSporumUtils#FACING_DIR_TO_SHAPE_MAP}.
     */
    public static VoxelShape getShapeForState(BlockState state) {
        Direction facing = getFacing(state);
        return FACING_DIR_TO_SHAPE_MAP.get(facing);
    }

    /**
     * @param state The spore blossom block state.
     * @return the {@link FlorumSporumUtils#AGE} property of {@code state}.
     */
    public static int getAge(BlockState state) {
        return state.get(AGE);
    }

    /**
     * @param state The spore blossom block state.
     * @return the {@link FlorumSporumUtils#FACING} property of {@code state}.
     */
    public static Direction getFacing(BlockState state) {
        return state.get(FACING);
    }

    /**
     * @param state The spore blossom block state.
     * @return the {@link FlorumSporumUtils#OPENNESS} property of {@code state}.
     */
    public static Openness getOpenness(BlockState state) {
        return state.get(OPENNESS);
    }

    /**
     * @param state The spore blossom block state.
     * @return true if the spore blossom is fully grown.
     */
    public static boolean isMaxAge(BlockState state) {
        return getAge(state) == 3;
    }

    /**
     * @param state The spore blossom block state.
     * @return true if the spore blossom has recoiled.
     */
    public static boolean isClosed(BlockState state) {
        return getOpenness(state) == Openness.CLOSED;
    }

    /**
     * @param state The spore blossom block state.
     * @return true if the spore blossom is as open as it can be for its current {@link FlorumSporumUtils#AGE}.
     */
    public static boolean isFullyOpen(BlockState state) {
        return getOpenness(state).ordinal() == getAge(state);
    }

    /**
     * @param state The spore blossom block state.
     * @return true if {@code state} is "invalid" and should be resolved as soon as a neighbor update is given.
     */
    public static boolean isInvalid(BlockState state) {
        return getOpenness(state).ordinal() > getAge(state);
    }

    /**
     * @param state The spore blossom block state.
     * @return the direction of the supporting block, which will always be opposite to the facing direction of the spore blossom.
     */
    public static Direction getSupportingDir(BlockState state) {
        return getFacing(state).getOpposite();
    }

    /**
     * @param pos The position of the spore blossom.
     * @param state The spore blossom block state.
     * @return the position of the supporting block.
     * @see FlorumSporumUtils#getSupportingDir(BlockState)
     */
    public static BlockPos getSupportingPos(BlockPos pos, BlockState state) {
        return pos.offset(getSupportingDir(state));
    }

    /**
     * @param world The world the spore blossom is in.
     * @param pos The position of the spore blossom.
     * @param state The spore blossom block state.
     * @return the state of the block supporting the spore blossom.
     * @see FlorumSporumUtils#getSupportingPos(BlockPos, BlockState)
     */
    public static BlockState getSupportingState(World world, BlockPos pos, BlockState state) {
        return world.getBlockState(getSupportingPos(pos, state));
    }

    /**
     * @param state The spore blossom block state.
     * @return a block state (initially {@code state}) with an incremented {@link FlorumSporumUtils#AGE} property and the maximum {@link FlorumSporumUtils#OPENNESS} for the new age.
     * @see FlorumSporumUtils#openFully(BlockState)
     */
    public static BlockState advanceAge(BlockState state) {
        return openFully(state.cycle(AGE));
    }

    /**
     * @param state The spore blossom block state.
     * @return a block state (initially {@code state}) with an {@link FlorumSporumUtils#OPENNESS} value of {@link Openness#CLOSED}.
     */
    public static BlockState close(BlockState state) {
        return state.with(OPENNESS, Openness.CLOSED);
    }

    /**
     * @param state The spore blossom block state.
     * @return a block state (initially {@code state}) with the maximum {@link FlorumSporumUtils#OPENNESS} for its age.
     */
    public static BlockState openFully(BlockState state) {
        int age = getAge(state);
        return state.with(OPENNESS, Openness.byOrdinal(age));
    }

    /**
     * @param state The spore blossom block state.
     * @return a block state (initially {@code state}) with an incremented {@link FlorumSporumUtils#OPENNESS} value.
     * @implNote This is used for slowly unfurling the spore blossom with a scheduled tick loop.
     */
    public static BlockState openNext(BlockState state) {
        return state.cycle(OPENNESS);
    }

    /**
     * @param world The world the spore blossom is in.
     * @param pos The position of the spore blossom.
     * @return true if there is at least 1 entity matching the {@link FlorumSporumUtils#CAUSES_RECOIL} predicate whose collision box intersects with {@code pos}.
     */
    public static boolean hasEntityAt(World world, BlockPos pos) {
        Box box = new Box(pos);
        List<Entity> entities = world.getEntitiesByClass(Entity.class, box, CAUSES_RECOIL);

        return !entities.isEmpty();
    }

    /**
     * Plays a spore blossom-exclusive sound.
     * @param world The world the spore blossom is in.
     * @param pos The position of the spore blossom.
     * @param open Whether to play the open or close sound.
     */
    public static void playSound(World world, BlockPos pos, boolean open) {
        SoundEvent sound = open ? FlorumSporumSoundEvents.SPORE_BLOSSOM_OPEN : FlorumSporumSoundEvents.SPORE_BLOSSOM_CLOSE;
        world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0F, MathHelper.nextBetween(world.getRandom(), 0.8F, 1.2F));
    }

    public static int lerp(float delta, int end) {
        return lerp(delta, 0, end);
    }

    public static int lerp(float delta, int start, int end) {
        return /*? if <=1.19.3 >>*/ /*(int)*/ MathHelper.lerp(delta, start, end);
    }

}