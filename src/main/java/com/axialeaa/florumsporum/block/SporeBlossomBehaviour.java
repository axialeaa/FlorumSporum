package com.axialeaa.florumsporum.block;

import com.axialeaa.florumsporum.block.property.Openness;
import com.axialeaa.florumsporum.data.registry.ModSoundEvents;
import com.mojang.math.OctahedralGroup;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;
import java.util.Map;

import static com.axialeaa.florumsporum.block.property.SporeBlossomProperties.*;

public class SporeBlossomBehaviour {

    // transforms downShape into "north shape", necessary for map ordering
    public static Map<Direction, VoxelShape> getShapeMap(VoxelShape downShape) {
        return Shapes.rotateAll(downShape, OctahedralGroup.BLOCK_ROT_X_270, new Vec3(0.5, 0.5, 0.5));
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

    public static BlockState advanceAge(ServerLevel level, BlockPos pos, BlockState state) {
        return openNoisily(level, pos, state.cycle(AGE));
    }

    public static void onFertilized(ServerLevel level, BlockPos pos, BlockState state) {
        if (isMaxAge(state))
            Block.popResource(level, pos, Items.SPORE_BLOSSOM.getDefaultInstance());
        else level.setBlockAndUpdate(pos, advanceAge(level, pos, state));
    }

    public static BlockState recoil(BlockState state) {
        return state.setValue(OPENNESS, Openness.CLOSED);
    }

    public static BlockState recoilNoisily(ServerLevel level, BlockPos pos, BlockState state) {
        level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        playSound(level, pos, false);

        return recoil(state);
    }

    public static BlockState open(BlockState state) {
        return state.setValue(OPENNESS, Openness.values()[getAge(state)]);
    }

    public static BlockState openNoisily(ServerLevel level, BlockPos pos, BlockState state) {
        level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        playSound(level, pos, true);

        return open(state);
    }

    public static BlockState unfurl(BlockState state) {
        return state.cycle(OPENNESS);
    }

    public static BlockState unfurlNoisily(ServerLevel level, BlockPos pos, BlockState state) {
        level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
        playSound(level, pos, true);

        return unfurl(state);
    }

    /**
     * @param level The level the spore blossom is in.
     * @param pos The position of the spore blossom.
     * @return true if there is at least 1 entity collision box intersects with {@code pos}.
     */
    public static boolean hasEntityAt(ServerLevel level, BlockPos pos) {
        AABB box = new AABB(pos);
        List<Entity> entities = level.getEntitiesOfClass(Entity.class, box, EntitySelector.NO_SPECTATORS);

        return !entities.isEmpty();
    }

    public static MapColor getMapColor(BlockState state) {
        return getFacing(state) == Direction.DOWN ? MapColor.PLANT : MapColor.COLOR_PINK;
    }

    public static void playSound(ServerLevel level, BlockPos pos, boolean opening) {
        SoundEvent sound = opening ? ModSoundEvents.SPORE_BLOSSOM_OPEN : ModSoundEvents.SPORE_BLOSSOM_CLOSE;
        float pitch = Mth.randomBetween(level.getRandom(), 0.8F, 1.2F);

        level.playSound(null, pos, sound, SoundSource.BLOCKS, 1.0F, pitch);
    }

}