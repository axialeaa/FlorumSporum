package com.axialeaa.florumsporum.block;

import com.axialeaa.florumsporum.block.property.Openness;
import com.axialeaa.florumsporum.data.registry.FlorumSporumGameRules;
import com.axialeaa.florumsporum.data.registry.FlorumSporumSoundEvents;
import com.axialeaa.florumsporum.item.SporeBlossomStack;
import com.axialeaa.florumsporum.mixin.sneeze.GoalAccessor;
import com.mojang.math.OctahedralGroup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.animal.panda.Panda;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SporeBlossomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.Map;

import static com.axialeaa.florumsporum.block.property.SporeBlossomProperties.*;

public class SporeBlossomBehaviour {

    // transforms downShape into "north shape", necessary for map ordering
    public static Map<Direction, VoxelShape> getShapeMap(VoxelShape downShape) {
        return Shapes.rotateAll(Shapes.rotate(downShape, OctahedralGroup.ROT_90_X_POS));
    }

    /**
     * @param state The spore blossom block state.
     * @return true if {@code state} is "invalid" and should be resolved as soon as a neighbor update is given.
     */
    public static boolean isInvalid(BlockState state) {
        return getOpenness(state).ordinal() > getAge(state);
    }

    public static boolean canShower(BlockState state) {
        return !isClosed(state) && isMaxAge(state) && getFacing(state) == Direction.DOWN;
    }

    public static BlockState advanceAge(ServerLevel serverLevel, BlockPos pos, BlockState state) {
        return openNoisily(serverLevel, pos, state.cycle(AGE));
    }

    public static void onFertilized(ServerLevel serverLevel, BlockPos pos, BlockState state, ItemStack stack) {
        if (isMaxAge(state))
            SporeBlossomStack.dropJuvenile(serverLevel, pos);
        else serverLevel.setBlockAndUpdate(pos, advanceAge(serverLevel, pos, state));

        stack.shrink(1);
    }

    public static BlockState recoil(BlockState state) {
        return state.setValue(OPENNESS, Openness.CLOSED);
    }

    public static BlockState recoilNoisily(ServerLevel serverLevel, BlockPos pos, BlockState state) {
        serverLevel.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        playSound(serverLevel, pos, false);

        return recoil(state);
    }

    public static BlockState open(BlockState state) {
        return state.setValue(OPENNESS, Openness.values()[getAge(state)]);
    }

    public static BlockState openNoisily(ServerLevel serverLevel, BlockPos pos, BlockState state) {
        serverLevel.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        playSound(serverLevel, pos, true);

        return open(state);
    }

    public static BlockState unfurl(BlockState state) {
        return state.cycle(OPENNESS);
    }

    public static BlockState unfurlNoisily(ServerLevel serverLevel, BlockPos pos, BlockState state) {
        serverLevel.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        playSound(serverLevel, pos, true);

        return unfurl(state);
    }

    /**
     * @param serverLevel The level the spore blossom is in.
     * @param pos The position of the spore blossom.
     * @return true if there is at least 1 entity collision box intersects with {@code pos}.
     */
    public static boolean hasEntityAt(ServerLevel serverLevel, BlockPos pos) {
        AABB box = new AABB(pos);
        List<Entity> entities = serverLevel.getEntitiesOfClass(Entity.class, box, EntitySelector.NO_SPECTATORS);

        return !entities.isEmpty();
    }

    public static MapColor getMapColor(BlockState state) {
        return getFacing(state) == Direction.DOWN ? MapColor.PLANT : MapColor.COLOR_PINK;
    }

    public static void playSound(ServerLevel serverLevel, BlockPos pos, boolean opening) {
        SoundEvent sound = opening ? FlorumSporumSoundEvents.SPORE_BLOSSOM_OPEN : FlorumSporumSoundEvents.SPORE_BLOSSOM_CLOSE;
        float pitch = Mth.randomBetween(serverLevel.getRandom(), 0.8F, 1.2F);

        serverLevel.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, pitch);
    }

    public static class PandaSneeze {

        private static int getSneezeTicks(ServerLevel serverLevel, Panda panda, boolean inSporeShower) {
            GameRules gameRules = serverLevel.getGameRules();

            if (inSporeShower)
                return gameRules.get(FlorumSporumGameRules.PANDA_SPORE_SHOWER_MAX_SNEEZE_INTERVAL);

            return gameRules.get(panda.isWeak() ? FlorumSporumGameRules.PANDA_WEAK_MAX_SNEEZE_INTERVAL : FlorumSporumGameRules.PANDA_DEFAULT_MAX_SNEEZE_INTERVAL);
        }

        public static boolean shouldSneeze(ServerLevel serverLevel, Panda panda, BlockPos pos, RandomSource random) {
            if (!panda.isBaby() || !panda.canPerformAction())
                return false;

            boolean inSporeShower = isInSporeShower(serverLevel, pos);
            int sneezeTicks = getSneezeTicks(serverLevel, panda, inSporeShower);

            return random.nextInt(GoalAccessor.invokeReducedTickDelay(sneezeTicks)) == 1;
        }

        private static boolean isInSporeShower(ServerLevel serverLevel, BlockPos pos) {
            BlockPos.MutableBlockPos mutable = pos.mutable();
            BlockState blockState = serverLevel.getBlockState(mutable);

            int i = 0;
            int checkRange = serverLevel.getGameRules().get(FlorumSporumGameRules.PANDA_SPORE_SHOWER_CHECK_DEPTH);

            while (i < checkRange && blockState.getCollisionShape(serverLevel, mutable).isEmpty()) {
                mutable.move(Direction.UP);
                i++;

                if (serverLevel.isInWorldBounds(mutable))
                    return false;

                blockState = serverLevel.getBlockState(mutable);

                if (blockState.getBlock() instanceof SporeBlossomBlock && canShower(blockState))
                    return true;
            }

            return false;
        }

    }

}