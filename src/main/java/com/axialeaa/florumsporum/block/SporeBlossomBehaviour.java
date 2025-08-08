package com.axialeaa.florumsporum.block;

import com.axialeaa.florumsporum.block.property.Openness;
import com.axialeaa.florumsporum.item.SporeBlossomStack;
import com.axialeaa.florumsporum.mixin.sneeze.GoalAccessor;
import com.axialeaa.florumsporum.registry.FlorumSporumTags;
import com.axialeaa.florumsporum.registry.FlorumSporumSoundEvents;
import com.google.common.collect.Maps;
import net.minecraft.SharedConstants;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.EnumMap;
import java.util.List;

import static com.axialeaa.florumsporum.block.property.SporeBlossomProperties.*;

public class SporeBlossomBehaviour {

    public static final int ENTITY_CHECK_INTERVAL = SharedConstants.TICKS_PER_SECOND * 3;
    public static final int UNFURL_INTERVAL = SharedConstants.TICKS_PER_SECOND / 2;

    public static final float PER_RANDOM_TICK_GROWTH_CHANCE = 0.1F;

    private static final EnumMap<Direction, VoxelShape> FACING_DIR_TO_SHAPE_MAP = Util.make(Maps.newEnumMap(Direction.class), map -> {
        map.put(Direction.DOWN,  Block.createCuboidShape(2, 13,2, 14, 16, 14));
        map.put(Direction.UP,    Block.createCuboidShape(2, 0, 2, 14, 3,  14));
        map.put(Direction.NORTH, Block.createCuboidShape(2, 2, 13,14, 14, 16));
        map.put(Direction.SOUTH, Block.createCuboidShape(2, 2, 0, 14, 14, 3 ));
        map.put(Direction.WEST,  Block.createCuboidShape(13,2, 2, 16, 14, 14));
        map.put(Direction.EAST,  Block.createCuboidShape(0, 2, 2, 3,  14, 14));
    });

    public static VoxelShape getShapeForState(BlockState state) {
        Direction facing = getFacing(state);
        return FACING_DIR_TO_SHAPE_MAP.get(facing);
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

    public static boolean isSupportedByCatalyst(World world, BlockPos pos, BlockState state) {
        return getSupportingState(world, pos, state).isIn(FlorumSporumTags.SPORE_BLOSSOM_CAN_GROW_ON);
    }

    public static boolean canShower(BlockState state) {
        return !isClosed(state) && isMaxAge(state) && getFacing(state) == Direction.DOWN;
    }

    public static BlockState advanceAge(World world, BlockPos pos, BlockState state) {
        return openNoisily(world, pos, state.cycle(AGE));
    }

    public static void onFertilized(World world, BlockPos pos, BlockState state, ItemStack stack) {
        if (isMaxAge(state))
            SporeBlossomStack.dropJuvenile(world, pos);
        else world.setBlockState(pos, advanceAge(world, pos, state));

        if (!world.isClient())
            stack.decrement(1);
    }

    public static BlockState recoil(BlockState state) {
        return state.with(OPENNESS, Openness.CLOSED);
    }

    public static BlockState recoilNoisily(World world, BlockPos pos, BlockState state) {
        world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        playSound(world, pos, false);

        return recoil(state);
    }

    public static BlockState open(BlockState state) {
        return state.with(OPENNESS, Openness.values()[getAge(state)]);
    }

    public static BlockState openNoisily(World world, BlockPos pos, BlockState state) {
        world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        playSound(world, pos, true);

        return open(state);
    }

    public static BlockState unfurl(BlockState state) {
        return state.cycle(OPENNESS);
    }

    public static BlockState unfurlNoisily(World world, BlockPos pos, BlockState state) {
        world.emitGameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        playSound(world, pos, true);

        return unfurl(state);
    }

    /**
     * @param world The world the spore blossom is in.
     * @param pos The position of the spore blossom.
     * @return true if there is at least 1 entity collision box intersects with {@code pos}.
     */
    public static boolean hasEntityAt(World world, BlockPos pos) {
        Box box = new Box(pos);
        List<Entity> entities = world.getEntitiesByClass(Entity.class, box, EntityPredicates.EXCEPT_SPECTATOR);

        return !entities.isEmpty();
    }

    public static MapColor getMapColor(BlockState state) {
        return getFacing(state) == Direction.DOWN ? MapColor.DARK_GREEN : MapColor.PINK;
    }

    public static void playSound(World world, BlockPos pos, boolean opening) {
        SoundEvent sound = opening ? FlorumSporumSoundEvents.SPORE_BLOSSOM_OPEN : FlorumSporumSoundEvents.SPORE_BLOSSOM_CLOSE;
        float pitch = MathHelper.nextBetween(world.getRandom(), 0.8F, 1.2F);

        world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0F, pitch);
    }

    public static class PandaSneeze {

        private static final int DEFAULT_SNEEZE_TICKS = 6000;
        private static final int WEAK_SNEEZE_TICKS = 500;
        private static final int IN_SPORE_SHOWER_SNEEZE_TICKS = 100;

        private static int getSneezeTicks(PandaEntity panda, boolean inSporeShower) {
            if (inSporeShower)
                return IN_SPORE_SHOWER_SNEEZE_TICKS;

            return panda.isWeak() ? WEAK_SNEEZE_TICKS : DEFAULT_SNEEZE_TICKS;
        }

        public static boolean shouldSneeze(World world, PandaEntity panda, BlockPos pos, Random random) {
            if (!panda.isBaby() || !panda.isIdle())
                return false;

            boolean inSporeShower = isInSporeShower(world, pos);
            int sneezeTicks = getSneezeTicks(panda, inSporeShower);

            return random.nextInt(GoalAccessor.invokeToGoalTicks(sneezeTicks)) == 1;
        }

        private static boolean isInSporeShower(World world, BlockPos pos) {
            BlockPos.Mutable mutable = pos.mutableCopy();
            int i = 0;

            while (i < 8 && world.getBlockState(mutable).getCollisionShape(world, mutable).isEmpty()) {
                mutable.move(Direction.UP);
                i++;

                BlockState potentialBlossom = world.getBlockState(mutable);

                if (potentialBlossom.getBlock() instanceof SporeBlossomBlock && canShower(potentialBlossom))
                    return true;
            }

            return false;
        }

    }

}