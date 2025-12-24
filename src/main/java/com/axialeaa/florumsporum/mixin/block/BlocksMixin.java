package com.axialeaa.florumsporum.mixin.block;

import com.axialeaa.florumsporum.block.SporeBlossomBehavior;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Blocks.class)
public class BlocksMixin {

    @ModifyExpressionValue(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=spore_blossom")), at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;mapColor(Lnet/minecraft/world/level/material/MapColor;)Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;", ordinal = 0))
    private static BlockBehaviour.Properties modifySettings(BlockBehaviour.Properties original) {
        return original.mapColor(SporeBlossomBehavior::getMapColor).randomTicks();
    }

}