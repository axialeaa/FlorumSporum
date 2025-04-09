package com.axialeaa.florumsporum.mixin.impl;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

//? if >=1.21.3
import net.minecraft.world.block.WireOrientation;

//? if >=1.21.4 {
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldView;
//?}

//? if >=1.21.5
import net.minecraft.entity.EntityCollisionHandler;

@Mixin(AbstractBlock.class)
public class AbstractBlockImplMixin {

    @WrapMethod(method = "onEntityCollision")
    public void onEntityCollisionImpl(BlockState state, World world, BlockPos pos, Entity entity,
                                      /*? if >=1.21.5*/
                                      EntityCollisionHandler handler,
                                      Operation<Void> original) {
        original.call(state, world, pos, entity /*? if >=1.21.5 >>*/ ,handler );
    }

    @WrapMethod(method = "randomTick")
    public void randomTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, Operation<Void> original) {
        original.call(state, world, pos, random);
    }

    @WrapMethod(method = "scheduledTick")
    public void scheduledTickImpl(BlockState state, ServerWorld world, BlockPos pos, Random random, Operation<Void> original) {
        original.call(state, world, pos, random);
    }

    //? if >=1.21.4 {
    @WrapMethod(method = "getPickStack")
    public ItemStack getPickStackImpl(WorldView world, BlockPos pos, BlockState state, boolean includeData, Operation<ItemStack> original) {
        return original.call(world, pos, state, includeData);
    }
    //?}

    @WrapMethod(method = "neighborUpdate")
    //? if >=1.21.3 {
    public void neighborUpdateImpl(BlockState state, World world, BlockPos pos, Block sourceBlock, WireOrientation wireOrientation, boolean notify, Operation<Void> original) {
        original.call(state, world, pos, sourceBlock, wireOrientation, notify);
    }
    //?} else {
    /*public void neighborUpdateImpl(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify, Operation<Void> original) {
        original.call(state, world, pos, sourceBlock, sourcePos, notify);
    }
    *///?}

    //? if >1.20.4 {
    @WrapMethod(method = "hasRandomTicks")
    public boolean hasRandomTicksImpl(BlockState state, Operation<Boolean> original) {
        return original.call(state);
    }
    //?}

}
