package com.axialeaa.florumsporum.mixin.impl;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.block.WireOrientation;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractBlock.class)
public class AbstractBlockImplMixin {

    @WrapMethod(method = "onEntityCollision")
    public void onEntityCollisionImpl(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler, Operation<Void> original) {
        original.call(state, world, pos, entity, handler);
    }

    @WrapMethod(method = "randomTick")
    public void randomTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, Operation<Void> original) {
        original.call(state, world, pos, random);
    }

    @WrapMethod(method = "scheduledTick")
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, Operation<Void> original) {
        original.call(state, world, pos, random);
    }

    @WrapMethod(method = "getPickStack")
    public ItemStack getPickStackImpl(WorldView world, BlockPos pos, BlockState state, boolean includeData, Operation<ItemStack> original) {
        return original.call(world, pos, state, includeData);
    }

    @WrapMethod(method = "neighborUpdate")
    public void neighborUpdateImpl(BlockState state, World world, BlockPos pos, Block sourceBlock, WireOrientation wireOrientation, boolean notify, Operation<Void> original) {
        original.call(state, world, pos, sourceBlock, wireOrientation, notify);
    }

    @WrapMethod(method = "hasRandomTicks")
    public boolean hasRandomTicksImpl(BlockState state, Operation<Boolean> original) {
        return original.call(state);
    }

}
