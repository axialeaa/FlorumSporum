package com.axialeaa.florumsporum.mixin.accessor;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Block.class)
public interface BlockAccessor {

    @Invoker("setDefaultState")
    void invokeSetDefaultState(BlockState state);

}
