package com.axialeaa.florumsporum.mixin.impl;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Block.class)
public abstract class BlockImplMixin extends BlockBehaviourImplMixin {

    @Shadow
    public abstract BlockState defaultBlockState();

    @Shadow
    protected abstract void registerDefaultState(BlockState blockState);

    @Shadow
    @Final
    protected StateDefinition<Block, BlockState> stateDefinition;

    @WrapMethod(method = "createBlockStateDefinition")
    public void createBlockStateDefinitionImpl(StateDefinition.Builder<Block, BlockState> builder, Operation<Void> original) {
        original.call(builder);
    }

    @WrapMethod(method = "getStateForPlacement")
    public BlockState getStateForPlacementImpl(BlockPlaceContext blockPlaceContext, Operation<BlockState> original) {
        return original.call(blockPlaceContext);
    }

    @WrapMethod(method = "setPlacedBy")
    public void setPlacedByImpl(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack, Operation<Void> original) {
        original.call(level, blockPos, blockState, livingEntity, itemStack);
    }

}