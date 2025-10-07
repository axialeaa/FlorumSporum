package com.axialeaa.florumsporum.mixin;

import com.axialeaa.florumsporum.block.SporeBlossomBehaviour;
import com.axialeaa.florumsporum.block.property.SporeBlossomProperties;
import com.axialeaa.florumsporum.data.registry.FlorumSporumBlockTags;
import com.axialeaa.florumsporum.mixin.impl.BlockImplMixin;
import com.axialeaa.florumsporum.item.SporeBlossomStack;
import com.axialeaa.florumsporum.block.property.Openness;
import com.axialeaa.florumsporum.particle.RaycastedSporeArea;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(SporeBlossomBlock.class)
public abstract class SporeBlossomBlockMixin extends BlockImplMixin {

    @Shadow @Final private static VoxelShape SHAPE;
    @Unique private static final Map<Direction, VoxelShape> VOXEL_SHAPE_MAP = SporeBlossomBehaviour.getShapeMap(SHAPE);

    @Inject(method = "<init>", at = @At("TAIL"))
    private void registerDefaultState(AbstractBlock.Settings settings, CallbackInfo ci) {
        this.setDefaultState(this.getDefaultState().with(SporeBlossomProperties.FACING, Direction.DOWN).with(SporeBlossomProperties.AGE, SporeBlossomProperties.MAX_AGE).with(SporeBlossomProperties.OPENNESS, Openness.FULL));
    }

    @WrapWithCondition(method = "randomDisplayTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticleClient(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V", ordinal = 0))
    private boolean shouldAddShowerParticles(World instance, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, @Local(argsOnly = true) BlockState state) {
        return SporeBlossomBehaviour.canShower(state);
    }

    @ModifyConstant(method = "randomDisplayTick", constant = @Constant(intValue = 14))
    public int addSporeArea(int original, @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) World world, @Local(argsOnly = true) BlockPos pos, @Local(argsOnly = true) Random random) {
        if (SporeBlossomProperties.isClosed(state))
            return 0;

        RaycastedSporeArea sporeArea = new RaycastedSporeArea(state, pos);

        int count = MathHelper.lerp((float) SporeBlossomProperties.getAge(state) / SporeBlossomProperties.MAX_AGE, 0, original);
        sporeArea.addClientParticles(world, random, count);

        return 0;
    }

    @WrapOperation(method = "canPlaceAt", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;sideCoversSmallSquare(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z"))
    private boolean isValidPlacementSurface(WorldView world, BlockPos _pos, Direction side, Operation<Boolean> original, @Local(argsOnly = true) BlockPos pos, @Local(argsOnly = true) BlockState state) {
        Direction facing = SporeBlossomProperties.getFacing(state);
        return original.call(world, pos.offset(facing.getOpposite()), facing);
    }

    @ModifyExpressionValue(method = "getStateForNeighborUpdate", at = @At(value = "FIELD", target = "Lnet/minecraft/util/math/Direction;UP:Lnet/minecraft/util/math/Direction;"))
    private Direction modifyUpdateCheckDirection(Direction original, @Local(argsOnly = true, ordinal = 0) BlockState state) {
        return SporeBlossomProperties.getFacing(state).getOpposite();
    }

    @ModifyReturnValue(method = "getOutlineShape", at = @At("RETURN"))
    private VoxelShape modifyShape(VoxelShape original, @Local(argsOnly = true) BlockState state) {
        return VOXEL_SHAPE_MAP.get(SporeBlossomProperties.getFacing(state));
    }

    @Override
    public void onPlacedImpl(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, Operation<Void> original) {
        super.onPlacedImpl(world, pos, state, placer, itemStack, original);
        world.setBlockState(pos, SporeBlossomBehaviour.open(state), Block.NOTIFY_LISTENERS);
    }

    @Override
    public ItemStack getPickStackImpl(WorldView world, BlockPos pos, BlockState state, boolean includeData, Operation<ItemStack> original) {
        return SporeBlossomStack.create(state);
    }

    @Override
    public void onEntityCollisionImpl(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler, boolean bl, Operation<Void> original) {
        if (!world.isClient() && !world.isReceivingRedstonePower(pos) && !SporeBlossomProperties.isClosed(state)) {
            world.setBlockState(pos, SporeBlossomBehaviour.recoilNoisily(world, pos, state));
            world.scheduleBlockTick(pos, this.asBlock(), SporeBlossomBehaviour.ENTITY_CHECK_INTERVAL);
        }

        super.onEntityCollisionImpl(state, world, pos, entity, handler, bl, original);
    }

    @Override
    public void neighborUpdateImpl(BlockState state, World world, BlockPos pos, Block sourceBlock, WireOrientation wireOrientation, boolean notify, Operation<Void> original) {
        super.neighborUpdateImpl(state, world, pos, sourceBlock, wireOrientation, notify, original);

        if (SporeBlossomProperties.isFullyOpen(state))
            return;

        if (world.isReceivingRedstonePower(pos) || SporeBlossomBehaviour.isInvalid(state))
            world.setBlockState(pos, SporeBlossomBehaviour.openNoisily(world, pos, state));
    }

    @Override
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, Operation<Void> original) {
        if (SporeBlossomBehaviour.hasEntityAt(world, pos))
            world.scheduleBlockTick(pos, this.asBlock(), SporeBlossomBehaviour.ENTITY_CHECK_INTERVAL);
        else if (!SporeBlossomProperties.isFullyOpen(state)) {
            world.setBlockState(pos, SporeBlossomBehaviour.unfurlNoisily(world, pos, state));
            world.scheduleBlockTick(pos, this.asBlock(), SporeBlossomBehaviour.UNFURL_INTERVAL);
        }

        super.scheduledTickImpl(state, world, pos, random, original);
    }

    @Nullable
    @Override
    public BlockState getPlacementStateImpl(ItemPlacementContext ctx, Operation<BlockState> original) {
        BlockState blockState = this.getDefaultState().with(SporeBlossomProperties.FACING, ctx.getSide());
        return blockState.canPlaceAt(ctx.getWorld(), ctx.getBlockPos()) ? blockState : null;
    }

    @Override
    public boolean hasRandomTicksImpl(BlockState state, Operation<Boolean> original) {
        return !SporeBlossomProperties.isMaxAge(state);
    }

    @Override
    public void randomTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, Operation<Void> original) {
        if (random.nextFloat() > SporeBlossomBehaviour.PER_RANDOM_TICK_GROWTH_CHANCE)
            return;

        BlockState supportState = world.getBlockState(pos.offset(SporeBlossomProperties.getFacing(state).getOpposite()));

        if (supportState.isIn(FlorumSporumBlockTags.SPORE_BLOSSOM_CAN_GROW_ON))
            world.setBlockState(pos, SporeBlossomBehaviour.advanceAge(world, pos, state));

        super.randomTickImpl(state, world, pos, random, original);
    }

    @Override
    public void appendPropertiesImpl(StateManager.Builder<Block, BlockState> builder, Operation<Void> original) {
        builder.add(SporeBlossomProperties.FACING, SporeBlossomProperties.AGE, SporeBlossomProperties.OPENNESS);
        super.appendPropertiesImpl(builder, original);
    }

}
