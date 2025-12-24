package com.axialeaa.florumsporum.particle;

import com.axialeaa.florumsporum.mixin.block.SporeBlossomBlockMixin;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;

import static com.axialeaa.florumsporum.block.property.SporeBlossomProperties.getFacing;

/**
 * Creates a box and spawns a certain number of {@link ParticleTypes#SPORE_BLOSSOM_AIR} particles inside it, making sure
 * there is a direct line of sight from the spore blossom to the particle position. This prevents particles being
 * created past walls; if the spore blossom is outside, the particles will stay outside.
 * @param state The spore blossom block state.
 * @param center The spore blossom position around which to spawn particles.
 * @see SporeBlossomBlockMixin#addSporeArea(int, BlockState, Level, BlockPos, RandomSource)
 */
public record RaycastedSporeArea(BlockState state, BlockPos center) {

    public static final int SPORE_RANGE = 10;

    public void addClientParticles(Level level, RandomSource randomSource, int count) {
        for (int i = 0; i < count; i++)
            this.addClientParticle(level, randomSource);
    }

    /**
     * Spawns a spore particle inside the box as long as there is a direct line of sight from the spore
     * blossom to the particle spawn position.
     *
     * @param level The level instance.
     * @param randomSource The randomSource instance.
     */
    private void addClientParticle(Level level, RandomSource randomSource) {
        Vec3 pos = this.getRandomPosInBox(randomSource);

        if (!this.hasUnblockedLineOfSight(level, pos))
            return;

        level.addAlwaysVisibleParticle(
            ParticleTypes.SPORE_BLOSSOM_AIR,
            pos.x(),
            pos.y(),
            pos.z(),
            0.0,
            0.0,
            0.0
        );
    }

    /**
     * Calculates a box measuring ({@link RaycastedSporeArea#SPORE_RANGE} * 2) ^ 3, shrunk in accordance with the direction of the block
     * the spore blossom is resting on. This prevents particles from attempting to spawn behind the spore blossom.
     */
    private AABB calculateSporeBox() {
        AABB box = new AABB(this.center).inflate(SPORE_RANGE);
        Direction supportDir = getFacing(state).getOpposite();

        return box.contract(
            supportDir.getStepX() * SPORE_RANGE,
            supportDir.getStepY() * SPORE_RANGE,
            supportDir.getStepZ() * SPORE_RANGE
        );
    }

    private Vec3 getRandomPosInBox(RandomSource randomSource) {
        AABB box = this.calculateSporeBox();
        
        return new Vec3(
            Mth.nextDouble(randomSource, box.minX, box.maxX),
            Mth.nextDouble(randomSource, box.minY, box.maxY),
            Mth.nextDouble(randomSource, box.minZ, box.maxZ)
        );
    }

    private boolean raycastMissed(Level level, ClipContext ctx) {
        BlockHitResult blockHitResult = level.clip(ctx);
        BlockPos blockPos = blockHitResult.getBlockPos();

        return blockPos.equals(this.center) || blockHitResult.getType() == HitResult.Type.MISS;
    }

    private boolean hasUnblockedLineOfSight(Level level, Vec3 vec) {
        ClipContext ctx = new ClipContext(
            vec,
            this.center.getCenter(),
            ClipContext.Block.OUTLINE,
            ClipContext.Fluid.NONE,
            CollisionContext.empty()
        );

        return this.raycastMissed(level, ctx);
    }


}