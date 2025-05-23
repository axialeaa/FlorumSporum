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

//? if <1.21.4 >=1.20.4
/*import net.minecraft.world.WorldView;*/

//? if <1.20.6 {
/*import net.minecraft.world.BlockView;
import net.minecraft.client.item.TooltipContext;
import org.jetbrains.annotations.Nullable;
*///?}

@Mixin(Block.class)
public class BlockImplMixin extends AbstractBlockImplMixin {

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

    //? if <1.21.4 {
    /*@WrapMethod(method = "getPickStack")
    public ItemStack getPickStackImpl(
            //? if <1.20.4 {
            /^BlockView
            ^///?} else
            WorldView
                world, BlockPos pos, BlockState state, Operation<ItemStack> original) {
        return original.call(world, pos, state);
    }
    *///?}

    //? <=1.20.4 {
    /*@WrapMethod(method = "hasRandomTicks")
    public boolean hasRandomTicksImpl(BlockState state, Operation<Boolean> original) {
        return original.call(state);
    }
    *///?}

}