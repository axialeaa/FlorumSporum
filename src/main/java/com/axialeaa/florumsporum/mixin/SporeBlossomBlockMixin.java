package com.axialeaa.florumsporum.mixin;

import com.axialeaa.florumsporum.mixin.accessor.BlockAccessor;
import com.axialeaa.florumsporum.mixin.impl.BlockImplMixin;
import com.axialeaa.florumsporum.util.Openness;
import com.axialeaa.florumsporum.util.RaycastSporeArea;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import /*$ random_import >>*/ net.minecraft.util.math.random.Random ;

import static com.axialeaa.florumsporum.util.FlorumSporumUtils.*;

@Mixin(SporeBlossomBlock.class)
public class SporeBlossomBlockMixin extends BlockImplMixin {

    @Unique private final Block thisBlock = Block.class.cast(this);

    @Inject(method = "<init>", at = @At("TAIL"))
    private void registerDefaultState(AbstractBlock.Settings settings, CallbackInfo ci) {
        ((BlockAccessor) thisBlock).invokeSetDefaultState(thisBlock.getDefaultState().with(FACING, Direction.DOWN).with(AGE, 3).with(OPENNESS, Openness.FULL));
    }

    @WrapWithCondition(method = "randomDisplayTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V", ordinal = 0))
    private boolean shouldShower(World instance, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, @Local(argsOnly = true) BlockState state) {
        return !isClosed(state) && isMaxAge(state) && getFacing(state) == Direction.DOWN;
    }

    @Inject(method = "randomDisplayTick", at = @At(value = "NEW", target = "()Lnet/minecraft/util/math/BlockPos$Mutable;", shift = At.Shift.BEFORE), cancellable = true)
    public void addSporeArea(BlockState state, World world, BlockPos pos, Random random, CallbackInfo ci) {
        if (isClosed(state)) {
            ci.cancel();
            return;
        }

        float delta = (float) getAge(state) / 3;

        int count = lerp(delta, RaycastSporeArea.MAX_SPORE_COUNT);
        int range = lerp(delta, RaycastSporeArea.MAX_SPORE_RANGE);

        RaycastSporeArea sporeArea = new RaycastSporeArea(state, pos, range);
        sporeArea.addParticles(world, random, count);

        ci.cancel();
    }

    @WrapOperation(method = "canPlaceAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;sideCoversSmallSquare(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z"))
    private boolean isValidPlacementSurface(WorldView world, BlockPos _pos, Direction side, Operation<Boolean> original, @Local(argsOnly = true) BlockPos pos, @Local(argsOnly = true) BlockState state) {
        return original.call(world, getSupportingPos(pos, state), getFacing(state));
    }

    @ModifyExpressionValue(method = "getStateForNeighborUpdate", at = @At(value = "FIELD", target = "Lnet/minecraft/util/math/Direction;UP:Lnet/minecraft/util/math/Direction;"))
    private Direction modifyUpdateCheckDirection(Direction original, @Local(argsOnly = true, ordinal = 0) BlockState state) {
        return getSupportingDir(state);
    }

    @ModifyReturnValue(method = "getOutlineShape", at = @At("RETURN"))
    private VoxelShape modifyShape(VoxelShape original, @Local(argsOnly = true) BlockState state) {
        return getShapeForState(state);
    }

    @Override
    public void onEntityCollisionImpl(BlockState state, World world, BlockPos pos, Entity entity, Operation<Void> original) {
        if (!world.isClient() && CAUSES_RECOIL.test(entity) && !world.isReceivingRedstonePower(pos) && !isClosed(state)) {
            world.setBlockState(pos, close(state));
            playSound(world, pos, false);

            scheduleBlockTick(world, pos, 1);
        }

        super.onEntityCollisionImpl(state, world, pos, entity, original);
    }

    @Override
    public void neighborUpdateImpl(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, Operation<Void> original) {
        if (!isFullyOpen(state) && (world.isReceivingRedstonePower(pos) || isInvalid(state))) {
            world.setBlockState(pos, openFully(state));
            playSound(world, pos, true);
        }

        super.neighborUpdateImpl(state, world, pos, sourceBlock, sourcePos, notify, original);
    }

    @Override
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, Operation<Void> original) {
        if (hasEntityAt(world, pos))
            scheduleBlockTick(world, pos, ENTITY_CHECK_INTERVAL);
        else if (!isFullyOpen(state)) {
            world.setBlockState(pos, openNext(state));
            playSound(world, pos, true);

            scheduleBlockTick(world, pos, UNFURL_INTERVAL);
        }

        super.scheduledTickImpl(state, world, pos, random, original);
    }

    @Nullable
    @Override
    public BlockState getPlacementStateImpl(ItemPlacementContext ctx, Operation<BlockState> original) {
        BlockState blockState = thisBlock.getDefaultState().with(FACING, ctx.getSide());
        return blockState.canPlaceAt(ctx.getWorld(), ctx.getBlockPos()) ? blockState : null;
    }

    @Override
    public boolean hasRandomTicksImpl(BlockState state, Operation<Boolean> original) {
        return !isMaxAge(state);
    }

    @Override
    public void randomTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, Operation<Void> original) {
        if (random.nextFloat() > 0.1)
            return;

        BlockState blockState = getSupportingState(world, pos, state);

        if (blockState.getBlock() instanceof MossBlock)
            world.setBlockState(pos, advanceAge(state));

        super.randomTickImpl(state, world, pos, random, original);
    }

    @Override
    public void appendPropertiesImpl(StateManager.Builder<Block, BlockState> builder, Operation<Void> original) {
        builder.add(FACING, AGE, OPENNESS);
        super.appendPropertiesImpl(builder, original);
    }

    @Unique
    private void scheduleBlockTick(World world, BlockPos pos, int delay) {
        /*$ schedule_block_tick*/ world.scheduleBlockTick(pos, thisBlock, delay);
    }

}
