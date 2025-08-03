package com.axialeaa.florumsporum.particle;

import com.axialeaa.florumsporum.block.SporeBlossomBehaviour;
import com.axialeaa.florumsporum.mixin.SporeBlossomBlockMixin;
import net.minecraft.block.BlockState;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import net.minecraft.block.ShapeContext;

/**
 * Creates a box and spawns a certain number of {@link ParticleTypes#SPORE_BLOSSOM_AIR} particles inside it, making sure
 * there is a direct line of sight from the spore blossom to the particle position. This prevents particles being
 * created past walls; if the spore blossom is outside, the particles will stay outside.
 * @param state The spore blossom block state.
 * @param center The spore blossom position around which to spawn particles.
 * @see SporeBlossomBlockMixin#addSporeArea(int, BlockState, World, BlockPos, Random)
 */
public record RaycastedSporeArea(BlockState state, BlockPos center) {

    public static final int SPORE_RANGE = 10;

    public void addClientParticles(ClientWorld world, Random random, int count) {
        for (int i = 0; i < count; i++)
            this.addClientParticle(world, random);
    }

    /**
     * Spawns a spore particle inside the box as long as there is a direct line of sight from the spore
     * blossom to the particle spawn position.
     *
     * @param world The world instance.
     * @param random The random instance.
     */
    private void addClientParticle(ClientWorld world, Random random) {
        Vec3d pos = this.getRandomPosInBox(random);

        if (!this.hasUnblockedLineOfSight(world, pos))
            return;

        world.addParticleClient(
            ParticleTypes.SPORE_BLOSSOM_AIR,
            pos.getX(),
            pos.getY(),
            pos.getZ(),
            0.0,
            0.0,
            0.0
        );
    }

    /**
     * Calculates a box measuring ({@link RaycastedSporeArea#SPORE_RANGE} * 2) ^ 3, shrunk in accordance with the direction of the block
     * the spore blossom is resting on. This prevents particles from attempting to spawn behind the spore blossom.
     */
    public Box calculateSporeBox() {
        Box box = new Box(this.center).expand(SPORE_RANGE);
        Direction supporting = SporeBlossomBehaviour.getSupportingDir(state);

        return box.shrink(
            supporting.getOffsetX() * SPORE_RANGE,
            supporting.getOffsetY() * SPORE_RANGE,
            supporting.getOffsetZ() * SPORE_RANGE
        );
    }

    private Vec3d getRandomPosInBox(Random random) {
        Box box = this.calculateSporeBox();

        return new Vec3d(
            MathHelper.nextDouble(random, box.getMin(Direction.Axis.X), box.getMax(Direction.Axis.X)),
            MathHelper.nextDouble(random, box.getMin(Direction.Axis.Y), box.getMax(Direction.Axis.Y)),
            MathHelper.nextDouble(random, box.getMin(Direction.Axis.Z), box.getMax(Direction.Axis.Z))
        );
    }

    private boolean didRaycastMiss(World world, RaycastContext ctx) {
        BlockHitResult blockHitResult = world.raycast(ctx);
        BlockPos blockPos = blockHitResult.getBlockPos();

        return blockPos.equals(this.center) || blockHitResult.getType() == HitResult.Type.MISS;
    }

    private boolean hasUnblockedLineOfSight(World world, Vec3d pos) {
        RaycastContext ctx = new RaycastContext(
            pos,
            Vec3d.of(this.center),
            RaycastContext.ShapeType.OUTLINE,
            RaycastContext.FluidHandling.NONE,
            ShapeContext.absent()
        );

        return this.didRaycastMiss(world, ctx);
    }


}