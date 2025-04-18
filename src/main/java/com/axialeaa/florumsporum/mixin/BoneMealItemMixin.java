package com.axialeaa.florumsporum.mixin;

import com.axialeaa.florumsporum.block.SporeBlossomBehaviour;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.block.SporeBlossomBlock;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//? if >=1.20.6 {
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import /*$ particle_util_import >>*/ net.minecraft.particle.ParticleUtil ;
//?}

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {

    @ModifyReturnValue(method = "useOnFertilizable", at = @At(value = "RETURN", ordinal = 1))
    private static boolean useOnSporeBlossom(boolean original, @Local(argsOnly = true) ItemStack stack, @Local(argsOnly = true) World world, @Local(argsOnly = true) BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);

        if (blockState.getBlock() instanceof SporeBlossomBlock) {
            SporeBlossomBehaviour.onFertilized(world, pos, blockState, stack);
            return true;
        }

        return original;
    }

    //? if >=1.20.6 {
    @Inject(method = "createParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;"))
    private static void createParticlesOnSporeBlossomFertilized(WorldAccess world, BlockPos pos, int count, CallbackInfo ci, @Local BlockState blockState) {
        if (blockState.getBlock() instanceof SporeBlossomBlock)
            ParticleUtil.spawnParticlesAround(world, pos, count, ParticleTypes.HAPPY_VILLAGER);
    }
    //?}

}
