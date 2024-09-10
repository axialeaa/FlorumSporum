package com.axialeaa.florumsporum.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Blocks.class)
public class BlocksMixin {

    @ModifyArg(method = "<clinit>", slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=spore_blossom")), at = @At(value = "INVOKE", target = "net/minecraft/block/SporeBlossomBlock.<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V", ordinal = 0))
    private static AbstractBlock.Settings addRandomTicks(AbstractBlock.Settings settings) {
        return settings.ticksRandomly();
    }

}
