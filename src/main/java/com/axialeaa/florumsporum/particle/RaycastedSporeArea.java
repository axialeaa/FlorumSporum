package com.axialeaa.florumsporum.particle;

import com.axialeaa.florumsporum.mixin.SporeBlossomBlockMixin;
import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

//? if >=1.20.4 {
import net.minecraft.block.ShapeContext;
//?} else {
/*import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.MinecraftClient;
*///?}

import static com.axialeaa.florumsporum.block.SporeBlossomBehaviour.*;

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

    public void addParticles(World world, Random random, int count) {
        for (int i = 0; i < count; i++)
            this.addParticle(world, random);
    }

    /**
     * Spawns a spore particle inside the box as long as there is a direct line of sight from the spore
     * blossom to the particle spawn position.
     *
     * @param world  The world instance.
     * @param random The random instance.
     * @see RaycastedSporeArea#hasUnblockedLineOfSight(World, Vec3d)
     */
    private void addParticle(World world, Random random) {
        Vec3d pos = this.getRandomPosInBox(random, this.calculateBox());

        if (!this.hasUnblockedLineOfSight(world, pos))
            return;

        //? if <1.21.5 {
        /*world.addParticle(ParticleTypes.SPORE_BLOSSOM_AIR, pos.getX(), pos.getY(), pos.getZ(), 0.0, 0.0, 0.0);
        *///?} else
        world.addParticleClient(ParticleTypes.SPORE_BLOSSOM_AIR, pos.getX(), pos.getY(), pos.getZ(), 0.0, 0.0, 0.0);
    }

    /**
     * Calculates a box measuring ({@link RaycastedSporeArea#SPORE_RANGE} * 2) ^ 3, shrunk in accordance with the direction of the block
     * the spore blossom is resting on. This prevents particles from attempting to spawn behind the spore blossom.
     */
    private Box calculateBox() {
        Box box = new Box(-SPORE_RANGE, -SPORE_RANGE, -SPORE_RANGE, SPORE_RANGE, SPORE_RANGE, SPORE_RANGE);
        Direction supporting = getSupportingDir(this.state);

        return box.shrink(
            supporting.getOffsetX() * SPORE_RANGE,
            supporting.getOffsetY() * SPORE_RANGE,
            supporting.getOffsetZ() * SPORE_RANGE
        );
    }

    private Vec3d getRandomPosInBox(Random random, Box box) {
        Direction.Axis xAxis = Direction.Axis.X;
        Direction.Axis yAxis = Direction.Axis.Y;
        Direction.Axis zAxis = Direction.Axis.Z;

        double minX = box.getMin(xAxis), minY = box.getMin(yAxis), minZ = box.getMin(zAxis);
        double maxX = box.getMax(xAxis), maxY = box.getMax(yAxis), maxZ = box.getMax(zAxis);

        return Vec3d.add(
            this.center,
            MathHelper.nextDouble(random, minX, maxX),
            MathHelper.nextDouble(random, minY, maxY),
            MathHelper.nextDouble(random, minZ, maxZ)
        );
    }

    private boolean hasUnblockedLineOfSight(World world, Vec3d pos) {
        //? if <1.20.4 {
        /*ClientPlayerEntity player = MinecraftClient.getInstance().player;

        if (player == null)
            return false;
        *///?}

        RaycastContext ctx = new RaycastContext(pos, Vec3d.of(this.center), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE,
            //? if >=1.20.4 {
            ShapeContext.absent()
            //?} else
            /*player*/
        );

        BlockHitResult blockHitResult = world.raycast(ctx);
        BlockPos blockPos = blockHitResult.getBlockPos();

        return blockPos.equals(this.center) || blockHitResult.getType() == HitResult.Type.MISS;
    }


}