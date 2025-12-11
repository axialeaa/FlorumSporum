package com.axialeaa.florumsporum.mixin.fertilize;

import com.axialeaa.florumsporum.block.SporeBlossomBehaviour;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ParticleUtils;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.SporeBlossomBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {

    @ModifyReturnValue(method = "growCrop", at = @At(value = "RETURN", ordinal = 1))
    private static boolean useOnSporeBlossom(boolean original, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) Level level, @Local(argsOnly = true) BlockPos blockPos) {
        BlockState blockState = level.getBlockState(blockPos);

        if (level instanceof ServerLevel serverLevel && blockState.getBlock() instanceof SporeBlossomBlock) {
            SporeBlossomBehaviour.onFertilized(serverLevel, blockPos, blockState, stack);
            return true;
        }

        return original;
    }

    @Inject(method = "addGrowthParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getBlock()Lnet/minecraft/world/level/block/Block;"))
    private static void createParticlesOnSporeBlossomFertilized(LevelAccessor levelAccessor, BlockPos blockPos, int i, CallbackInfo ci, @Local BlockState blockState) {
        if (blockState.getBlock() instanceof SporeBlossomBlock)
            ParticleUtils.spawnParticleInBlock(levelAccessor, blockPos, i, ParticleTypes.HAPPY_VILLAGER);
    }

}
