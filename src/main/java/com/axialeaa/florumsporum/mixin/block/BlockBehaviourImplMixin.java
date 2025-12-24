package com.axialeaa.florumsporum.mixin.block;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockBehaviour.class)
public abstract class BlockBehaviourImplMixin {

    @Shadow protected abstract Block asBlock();

    @WrapMethod(method = "entityInside")
    public void entityInsideImpl(BlockState blockState, Level level, BlockPos blockPos, Entity entity, InsideBlockEffectApplier insideBlockEffectApplier, boolean bl, Operation<Void> original) {
        original.call(blockState, level, blockPos, entity, insideBlockEffectApplier, bl);
    }

    @WrapMethod(method = "randomTick")
    public void randomTickImpl(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, Operation<Void> original) {
        original.call(blockState, serverLevel, blockPos, randomSource);
    }

    @WrapMethod(method = "tick")
    public void tickImpl(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource, Operation<Void> original) {
        original.call(blockState, serverLevel, blockPos, randomSource);
    }

    @WrapMethod(method = "getCloneItemStack")
    public ItemStack getCloneItemStackImpl(LevelReader levelReader, BlockPos blockPos, BlockState blockState, boolean bl, Operation<ItemStack> original) {
        return original.call(levelReader, blockPos, blockState, bl);
    }

    @WrapMethod(method = "neighborChanged")
    public void neighborChangedImpl(BlockState blockState, Level level, BlockPos blockPos, Block block, Orientation orientation, boolean bl, Operation<Void> original) {
        original.call(blockState, level, blockPos, block, orientation, bl);
    }

    @WrapMethod(method = "isRandomlyTicking")
    public boolean isRandomlyTickingImpl(BlockState state, Operation<Boolean> original) {
        return original.call(state);
    }

}
