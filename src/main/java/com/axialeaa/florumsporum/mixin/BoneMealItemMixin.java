package com.axialeaa.florumsporum.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SporeBlossomBlock;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.axialeaa.florumsporum.util.FlorumSporumUtils.*;

@Mixin(BoneMealItem.class)
public class BoneMealItemMixin {

    @Inject(method = "useOnFertilizable", at = @At("TAIL"))
    private static void fertilizeSporeBlossom(ItemStack stack, World world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();

        if (!(block instanceof SporeBlossomBlock))
            return;

        if (isMaxAge(blockState))
            Block.dropStack(world, pos, new ItemStack(block));
        else world.setBlockState(pos, advanceAge(world, pos, blockState));
    }

}
