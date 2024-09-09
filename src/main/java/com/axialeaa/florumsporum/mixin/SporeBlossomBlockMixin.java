package com.axialeaa.florumsporum.mixin;

import com.axialeaa.florumsporum.util.Openness;
import com.axialeaa.florumsporum.util.SporeBlossomUtils;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import /*$ random_import*/ net.minecraft.util.math.random.Random;

/*? if <=1.19.2*/ /*import net.minecraft.world.BlockView;*/

import static com.axialeaa.florumsporum.util.SporeBlossomUtils.*;

/**
 * This is the main class that handles the modified logic for the spore blossom. It uses extending and overriding as well as interface method implementation so it's incompatible with other mods doing the same thing as me, but there's if you've installed two mods with such similar functionalities, you've probably made a mistake worth a crash report anyway!
 */
/*? if <=1.20.4*/ /*@SuppressWarnings("deprecation")*/
@Mixin(SporeBlossomBlock.class)
public abstract class SporeBlossomBlockMixin extends Block implements Fertilizable {

    @Shadow /*$ can_place_at_access_modifier >>*/ protected abstract boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos);

    public SporeBlossomBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void registerDefaultState(Settings settings, CallbackInfo ci) {
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.DOWN).with(AGE, 3).with(OPENNESS, Openness.FULL));
    }

    @WrapWithCondition(method = "randomDisplayTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V", ordinal = 0))
    private boolean shouldCreateShower(World instance, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, @Local(argsOnly = true) BlockState state) {
        return !isFullyClosed(state) && isMaxAge(state) && getFacing(state) == Direction.DOWN;
    }

    /**
     * @return the number of iterations for which to summon air particles.
     */
    @ModifyConstant(method = "randomDisplayTick", constant = @Constant(intValue = 14))
    private int modifyIterationCount(int original, @Local(argsOnly = true) BlockState state) {
        if (isFullyClosed(state))
            return 0;

        float delta = (float) Math.min(getOpenness(state).ordinal(), getAge(state)) / 3;

        return /*? if <=1.19.3 >>*/ /*(int)*/ MathHelper.lerp(delta, 0, original);
    }

    @WrapOperation(method = "canPlaceAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;sideCoversSmallSquare(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z"))
    private boolean isValidPlacementSurface(WorldView world, BlockPos _pos, Direction side, Operation<Boolean> original, @Local(argsOnly = true) BlockPos pos, @Local(argsOnly = true) BlockState state) {
        Direction facing = getFacing(state);
        return original.call(world, getSupportingPos(pos, state), facing);
    }

    @ModifyExpressionValue(method = "getStateForNeighborUpdate", at = @At(value = "FIELD", target = "Lnet/minecraft/util/math/Direction;UP:Lnet/minecraft/util/math/Direction;"))
    private Direction getUpdateCheckDirection(Direction original, @Local(argsOnly = true, ordinal = 0) BlockState state) {
        Direction facing = getFacing(state);
        return facing.getOpposite();
    }

    @ModifyReturnValue(method = "getOutlineShape", at = @At("RETURN"))
    private VoxelShape getShapeForState(VoxelShape original, @Local(argsOnly = true) BlockState state) {
        Direction facing = getFacing(state);
        return getShapeForDirection(facing);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient() && hasEntityAt(world, pos) && !world.isReceivingRedstonePower(pos) && closeFully(world, pos, state))
            scheduleBlockTick(world, pos, 1);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (world.isReceivingRedstonePower(pos))
            openFully(world, pos, state);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (hasEntityAt(world, pos))
            scheduleBlockTick(world, pos, 60);
        else if (openNext(world, pos, state))
            scheduleBlockTick(world, pos, 10);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        World world = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        BlockState blockState = this.getDefaultState().with(FACING, ctx.getSide());

        return blockState.canPlaceAt(world, blockPos) ? blockState : null;
    }

    @Override
    public boolean isFertilizable(/*$ world_view_arg >>*/ WorldView world, BlockPos pos, BlockState state
        /*? if <=1.20.1 */ /*, boolean isClient*/
    ) {
        return true;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    /**
     * Increases the {@link SporeBlossomUtils#AGE} blockstate property by 1 every time the spore blossom is bonemealed, unless the spore blossom is at the maximum age. In which case, it will drop a new spore blossom as an item.
     */
    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (!advanceAge(world, pos, state))
            dropStack(world, pos, new ItemStack(this));
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return !isMaxAge(state);
    }

    /**
     * Increases the {@link SporeBlossomUtils#AGE} blockstate property every 1 in 10 randomTicks, while the spore blossom is resting on moss.
     */
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (random.nextFloat() < 0.1 && world.getBlockState(getSupportingPos(pos, state)).getBlock() instanceof MossBlock)
            advanceAge(world, pos, state);
    }

    /**
     * Adds the {@link SporeBlossomUtils#FACING}, {@link SporeBlossomUtils#AGE} and {@link SporeBlossomUtils#OPENNESS} blockstate properties to the spore blossom.
     */
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, AGE, OPENNESS);
    }

    @Unique
    private void scheduleBlockTick(World world, BlockPos pos, int delay) {
        /*$ schedule_block_tick*/ world.scheduleBlockTick(pos, this, delay);
    }

}
