package com.axialeaa.florumsporum.block;

import com.axialeaa.florumsporum.block.property.Openness;
import com.axialeaa.florumsporum.mixin.SporeBlossomBlockMixin;
import com.axialeaa.florumsporum.registry.FlorumSporumBlockTags;
import com.axialeaa.florumsporum.registry.FlorumSporumSoundEvents;
import com.google.common.collect.Maps;
import net.minecraft.SharedConstants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.entity.Entity;
import net.minecraft.predicate.entity.EntityPredicates;
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
import net.minecraft.world.event.GameEvent;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

//? if <=1.21.1
/*import net.minecraft.state.property.DirectionProperty;*/

/**
 * The purpose of this class is to store static fields and methods used by {@link SporeBlossomBlockMixin SporeBlossomBlockMixin} without needing to make them private. This allows them to be called outside of that mixin.
 */
public class SporeBlossomBehaviour {

    public static final int
        ENTITY_CHECK_INTERVAL = SharedConstants.TICKS_PER_SECOND * 3,
        UNFURL_INTERVAL = SharedConstants.TICKS_PER_SECOND / 2;

    public static final float PER_RANDOM_TICK_GROWTH_CHANCE = 0.1F;
    public static final IntProperty AGE = Properties.AGE_3;

    public static final int
        MAX_AGE = 3,
        AGE_COUNT = MAX_AGE + 1;

    public static final /*$ direction_property >>*/ EnumProperty<Direction> FACING = Properties.FACING;
    public static final EnumProperty<Openness> OPENNESS = EnumProperty.of("openness", Openness.class);

    public static final Predicate<Entity> CAUSES_RECOIL = EntityPredicates.VALID_LIVING_ENTITY.and(EntityPredicates.EXCEPT_SPECTATOR);
    public static final Function<BlockState, MapColor> STATE_TO_MAP_COLOR = state -> getFacing(state) == Direction.DOWN ? MapColor.DARK_GREEN : MapColor.PINK;

    private static final EnumMap<Direction, VoxelShape> FACING_DIR_TO_SHAPE_MAP = Util.make(Maps.newEnumMap(Direction.class), map -> {
        map.put(Direction.DOWN,  Block.createCuboidShape(2.0, 13.0, 2.0, 14.0, 16.0, 14.0));
        map.put(Direction.UP,    Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 3.0, 14.0));
        map.put(Direction.NORTH, Block.createCuboidShape(2.0, 2.0, 13.0, 14.0, 14.0, 16.0));
        map.put(Direction.SOUTH, Block.createCuboidShape(2.0, 2.0, 0.0, 14.0, 14.0, 3.0));
        map.put(Direction.WEST,  Block.createCuboidShape(13.0, 2.0, 2.0, 16.0, 14.0, 14.0));
        map.put(Direction.EAST,  Block.createCuboidShape(0.0, 2.0, 2.0, 3.0, 14.0, 14.0));
    });

    public static VoxelShape getShapeForState(BlockState state) {
        Direction facing = getFacing(state);
        return FACING_DIR_TO_SHAPE_MAP.get(facing);
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

    public static boolean isMaxAge(BlockState state) {
        return getAge(state) == MAX_AGE;
    }

    public static float getAgeDelta(BlockState state) {
        return (float) getAge(state) / MAX_AGE;
    }

    public static boolean isClosed(BlockState state) {
        return getOpenness(state) == Openness.CLOSED;
    }

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

    public static Direction getSupportingDir(BlockState state) {
        return getFacing(state).getOpposite();
    }

    public static BlockPos getSupportingPos(BlockPos pos, BlockState state) {
        return pos.offset(getSupportingDir(state));
    }

    public static BlockState getSupportingState(World world, BlockPos pos, BlockState state) {
        return world.getBlockState(getSupportingPos(pos, state));
    }

    public static boolean isSupportedByMoss(World world, BlockPos pos, BlockState state) {
        return getSupportingState(world, pos, state).isIn(FlorumSporumBlockTags.SPORE_BLOSSOM_CAN_GROW_ON);
    }

    public static BlockState advanceAge(World world, BlockPos pos, BlockState state) {
        return openFullyWithSoundAndEvent(world, pos, state.cycle(AGE));
    }

    public static BlockState recoil(BlockState state) {
        return state.with(OPENNESS, Openness.CLOSED);
    }

    public static BlockState recoilWithSoundAndEvent(World world, BlockPos pos, BlockState state) {
        world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        playSound(world, pos, false);

        return recoil(state);
    }

    public static BlockState openFully(BlockState state) {
        return state.with(OPENNESS, Openness.byAge(state));
    }

    public static BlockState openFullyWithSoundAndEvent(World world, BlockPos pos, BlockState state) {
        world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        playSound(world, pos, true);

        return openFully(state);
    }

    public static BlockState unfurl(BlockState state) {
        return state.cycle(OPENNESS);
    }

    public static BlockState unfurlWithSoundAndEvent(World world, BlockPos pos, BlockState state) {
        world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        playSound(world, pos, true);

        return unfurl(state);
    }

    /**
     * @param world The world the spore blossom is in.
     * @param pos The position of the spore blossom.
     * @return true if there is at least 1 entity matching the {@link SporeBlossomBehaviour#CAUSES_RECOIL} predicate whose collision box intersects with {@code pos}.
     */
    public static boolean hasEntityAt(World world, BlockPos pos) {
        Box box = new Box(pos);
        List<Entity> entities = world.getEntitiesByClass(Entity.class, box, CAUSES_RECOIL);

        return !entities.isEmpty();
    }

    public static void playSound(World world, BlockPos pos, boolean open) {
        SoundEvent sound = open ? FlorumSporumSoundEvents.SPORE_BLOSSOM_OPEN : FlorumSporumSoundEvents.SPORE_BLOSSOM_CLOSE;
        world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0F, MathHelper.nextBetween(world.getRandom(), 0.8F, 1.2F));
    }

}