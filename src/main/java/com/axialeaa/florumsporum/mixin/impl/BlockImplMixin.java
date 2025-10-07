package com.axialeaa.florumsporum.mixin.impl;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Block.class)
public abstract class BlockImplMixin extends AbstractBlockImplMixin {

    @Shadow protected abstract void setDefaultState(BlockState state);
    @Shadow public abstract BlockState getDefaultState();

    @WrapMethod(method = "appendProperties")
    public void appendPropertiesImpl(StateManager.Builder<Block, BlockState> builder, Operation<Void> original) {
        original.call(builder);
    }

    @WrapMethod(method = "getPlacementState")
    public BlockState getPlacementStateImpl(ItemPlacementContext ctx, Operation<BlockState> original) {
        return original.call(ctx);
    }

    @WrapMethod(method = "onPlaced")
    public void onPlacedImpl(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, Operation<Void> original) {
        original.call(world, pos, state, placer, itemStack);
    }

}