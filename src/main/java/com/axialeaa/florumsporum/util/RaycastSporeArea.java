package com.axialeaa.florumsporum.util;

import com.axialeaa.florumsporum.mixin.SporeBlossomBlockMixin;
import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import /*$ random_import >>*/ net.minecraft.util.math.random.Random ;
import /*$ fifth_raycast_import >>*/ net.minecraft.block.ShapeContext ;

/**
 * Creates a box and spawns a certain number of {@link ParticleTypes#SPORE_BLOSSOM_AIR} particles inside it, making sure
 * there is a direct line of sight from the spore blossom to the particle position. This prevents particles being
 * created past walls; if the spore blossom is outside, the particles will stay outside.
 * @param state  The spore blossom block state.
 * @param center The spore blossom position around which to spawn particles.
 * @see SporeBlossomBlockMixin#addSporeArea(BlockState, World, BlockPos, Random, CallbackInfo)
 */
public record RaycastSporeArea(BlockState state, BlockPos center, int range) {

    public static final int MAX_SPORE_COUNT = 14;
    public static final int MAX_SPORE_RANGE = 10;

    /**
     * Spawns {@code count} spore particles inside the box as long as there is a direct line of sight from the spore
     * blossom to the particle spawn position.
     *
     * @param world  The world instance.
     * @param random The random instance.
     * @param count  The number of particles to spawn.
     * @see RaycastSporeArea#hasLineOfSight(World, Vec3d)
     */
    public void addParticles(World world, Random random, int count) {
        for (int i = 0; i < count; i++) {
            Vec3d pos = this.getRandomPosInBox(random);

            if (!this.hasLineOfSight(world, pos))
                continue;

            world.addParticle(
                ParticleTypes.SPORE_BLOSSOM_AIR,
                pos.getX(),
                pos.getY(),
                pos.getZ(),
                0.0,
                0.0,
                0.0
            );
        }
    }

    /**
     * Calculates a box measuring {@link RaycastSporeArea#range}^3, shrunk in accordance with the direction of the block
     * the spore blossom is resting on. This prevents particles from attempting to spawn behind the spore blossom.
     */
    private Box calculateOrGetBox() {
        Box box = new Box(-this.range, -this.range, -this.range, this.range, this.range, this.range);
        Direction supporting = FlorumSporumUtils.getSupportingDir(this.state);

        return box.shrink(
            supporting.getOffsetX() * this.range,
            supporting.getOffsetY() * this.range,
            supporting.getOffsetZ() * this.range
        );
    }

    /**
     * @param random The random instance.
     * @return A set of three doubles representing the coordinates of a random position inside the box calculated by
     * {@link RaycastSporeArea#calculateOrGetBox()}.
     */
    private Vec3d getRandomPosInBox(Random random) {
        Box box = this.calculateOrGetBox();

        Direction.Axis xAxis = Direction.Axis.X;
        Direction.Axis yAxis = Direction.Axis.Y;
        Direction.Axis zAxis = Direction.Axis.Z;

        double minX = box.getMin(xAxis), minY = box.getMin(yAxis), minZ = box.getMin(zAxis);
        double maxX = box.getMax(xAxis), maxY = box.getMax(yAxis), maxZ = box.getMax(zAxis);

        Vec3d vec3d = Vec3d.ofCenter(this.center);

        return vec3d.add(
            MathHelper.nextDouble(random, minX, maxX),
            MathHelper.nextDouble(random, minY, maxY),
            MathHelper.nextDouble(random, minZ, maxZ)
        );
    }

    /**
     * @param world The world instance.
     * @param pos   The particle spawn position.
     * @return true if there is an unobstructed line of sight from the spore blossom to {@code pos}, taking into account
     * collidable blocks along the way.
     */
    private boolean hasLineOfSight(World world, Vec3d pos) {
        Vec3d center = Vec3d.ofCenter(this.center);

        //? if <1.20.4 {
        /*if (MinecraftClient.getInstance().player == null)
            return false;
        *///?}

        RaycastContext ctx = new RaycastContext(pos, center, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, /*$ fifth_raycast_arg >>*/ ShapeContext.absent() );

        BlockHitResult blockHitResult = world.raycast(ctx);
        BlockPos blockPos = blockHitResult.getBlockPos();

        return blockPos.equals(this.center) || blockHitResult.getType() == HitResult.Type.MISS;
    }


}