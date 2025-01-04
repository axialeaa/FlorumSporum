package com.axialeaa.florumsporum.mixin;

import com.axialeaa.florumsporum.mixin.accessor.BlockAccessor;
import com.axialeaa.florumsporum.mixin.impl.BlockImplMixin;
import com.axialeaa.florumsporum.util.Openness;
import com.axialeaa.florumsporum.util.RaycastedSporeArea;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

//? if >=1.20.6 {
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.item.Item;
import /*$ tooltip_type_import >>*/ net.minecraft.item.tooltip.TooltipType ;
//?} else {
/*import net.minecraft.nbt.NbtCompound;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.world.BlockView;
*///?}

//? if >=1.21.3
import net.minecraft.world.block.WireOrientation;

import static com.axialeaa.florumsporum.util.FlorumSporumUtils.*;

@Mixin(SporeBlossomBlock.class)
public class SporeBlossomBlockMixin extends BlockImplMixin {

    @Unique private final Block asBlock = (Block) (Object) this;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void registerDefaultState(AbstractBlock.Settings settings, CallbackInfo ci) {
        ((BlockAccessor) asBlock).invokeSetDefaultState(this.asBlock.getDefaultState().with(FACING, Direction.DOWN).with(AGE, 3).with(OPENNESS, Openness.FULL));
    }

    @WrapWithCondition(method = "randomDisplayTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V", ordinal = 0))
    private boolean shouldShower(World instance, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, @Local(argsOnly = true) BlockState state) {
        return !isClosed(state) && isMaxAge(state) && getFacing(state) == Direction.DOWN;
    }

    @ModifyConstant(method = "randomDisplayTick", constant = @Constant(intValue = 14))
    public int addSporeArea(int original, @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) World world, @Local(argsOnly = true) BlockPos pos, @Local(argsOnly = true) Random random) {
        if (!isClosed(state)) {
            int count = MathHelper.lerp(getAgeDelta(state), 0, original);
            RaycastedSporeArea sporeArea = new RaycastedSporeArea(state, pos);

            sporeArea.addParticles(world, random, count);
        }

        return 0;
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
    public void onPlacedImpl(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, Operation<Void> original) {
        super.onPlacedImpl(world, pos, state, placer, itemStack, original);
        world.setBlockState(pos, openFully(state), Block.NOTIFY_LISTENERS);
    }

    @Override
    //? if <1.20.4 {
    /*public ItemStack getPickStackImpl(BlockView world, BlockPos pos, BlockState state, Operation<ItemStack> original) {
        ItemStack itemStack = super.getPickStackImpl(world, pos, state, original);
        return addDataForAge(itemStack, getAge(state));
    }
    *///?} else {
    public ItemStack getPickStackImpl(WorldView world, BlockPos pos, BlockState state,
                                      //? if >=1.21.4
                                      boolean includeData,
                                      Operation<ItemStack> original) {
        ItemStack itemStack = super.getPickStackImpl(world, pos, state, /*? if >=1.21.4 >>*/ includeData, original);
        return addDataForAge(itemStack, getAge(state));
    }
    //?}

    //? if >=1.20.6 {
    @Override
    public void appendTooltipImpl(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options, Operation<Void> original) {
        super.appendTooltipImpl(stack, context, tooltip, options, original);

        BlockStateComponent blockStateComponent = stack.getOrDefault(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT);

        var componentValue = blockStateComponent.getValue(AGE);
        int age = componentValue == null ? 3 : componentValue;

        tooltip.add(Text.translatable("block.spore_blossom.growth_stage", age + 1).formatted(Formatting.GRAY));
    }
    //?} else {
    /*@Override
    public void appendTooltipImpl(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options, Operation<Void> original) {
        super.appendTooltipImpl(stack, world, tooltip, options, original);

        NbtCompound compound = stack.getSubNbt("BlockStateTag");
        int age = 3;

        try {
            if (compound != null && compound.contains(AGE.getName()))
                age = Integer.parseInt(compound.getString(AGE.getName()));
        }
        catch (NumberFormatException ignored) {}

        tooltip.add(Text.translatable("block.spore_blossom.growth_stage", age + 1).formatted(Formatting.GRAY));
    }
    *///?}

    @Override
    public void onEntityCollisionImpl(BlockState state, World world, BlockPos pos, Entity entity, Operation<Void> original) {
        if (!world.isClient() && CAUSES_RECOIL.test(entity) && !world.isReceivingRedstonePower(pos) && !isClosed(state)) {
            world.setBlockState(pos, recoilWithSound(world, pos, state));
            world.scheduleBlockTick(pos, this.asBlock, 1);
        }

        super.onEntityCollisionImpl(state, world, pos, entity, original);
    }

    //? if >=1.21.3 {
    @Override
    public void neighborUpdateImpl(BlockState state, World world, BlockPos pos, Block sourceBlock, WireOrientation wireOrientation, boolean notify, Operation<Void> original) {
        neighborUpdate(state, world, pos);
        super.neighborUpdateImpl(state, world, pos, sourceBlock, wireOrientation, notify, original);
    }
    //?} else {
    /*@Override
    public void neighborUpdateImpl(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, Operation<Void> original) {
        neighborUpdate(state, world, pos);
        super.neighborUpdateImpl(state, world, pos, sourceBlock, sourcePos, notify, original);
    }
    *///?}

    @Override
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, Operation<Void> original) {
        if (hasEntityAt(world, pos))
            world.scheduleBlockTick(pos, this.asBlock, ENTITY_CHECK_INTERVAL);
        else if (!isFullyOpen(state)) {
            world.setBlockState(pos, unfurlWithSound(world, pos, state));
            world.scheduleBlockTick(pos, this.asBlock, UNFURL_INTERVAL);
        }

        super.scheduledTickImpl(state, world, pos, random, original);
    }

    @Nullable
    @Override
    public BlockState getPlacementStateImpl(ItemPlacementContext ctx, Operation<BlockState> original) {
        BlockState blockState = this.asBlock.getDefaultState().with(FACING, ctx.getSide());
        return blockState.canPlaceAt(ctx.getWorld(), ctx.getBlockPos()) ? blockState : null;
    }

    @Override
    public boolean hasRandomTicksImpl(BlockState state, Operation<Boolean> original) {
        return !isMaxAge(state);
    }

    @Override
    public void randomTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, Operation<Void> original) {
        if (random.nextFloat() > PER_RANDOM_TICK_GROWTH_CHANCE)
            return;

        if (isSupportedByMoss(world, pos, state))
            world.setBlockState(pos, advanceAge(world, pos, state));

        super.randomTickImpl(state, world, pos, random, original);
    }

    @Override
    public void appendPropertiesImpl(StateManager.Builder<Block, BlockState> builder, Operation<Void> original) {
        builder.add(FACING, AGE, OPENNESS);
        super.appendPropertiesImpl(builder, original);
    }

    @Unique
    private static void neighborUpdate(BlockState state, World world, BlockPos pos) {
        if (!isFullyOpen(state) && (world.isReceivingRedstonePower(pos) || isInvalid(state))) {
            world.setBlockState(pos, openFully(state));
            playSound(world, pos, true);
        }
    }

}
