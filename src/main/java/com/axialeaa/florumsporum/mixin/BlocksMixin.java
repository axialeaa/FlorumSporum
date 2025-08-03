package com.axialeaa.florumsporum.mixin;

import com.axialeaa.florumsporum.block.SporeBlossomBehaviour;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

import net.minecraft.block.MapColor;

@Mixin(Blocks.class)
public class BlocksMixin {

    @WrapOperation(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=spore_blossom")), at = @At(value = "INVOKE", target = "Lnet/minecraft/block/AbstractBlock$Settings;mapColor(Lnet/minecraft/block/MapColor;)Lnet/minecraft/block/AbstractBlock$Settings;", ordinal = 0))
    private static AbstractBlock.Settings modifySettings(AbstractBlock.Settings instance, MapColor color, Operation<AbstractBlock.Settings> original) {
        return original.call(instance, color).mapColor(SporeBlossomBehaviour::getMapColor).ticksRandomly();
    }

}