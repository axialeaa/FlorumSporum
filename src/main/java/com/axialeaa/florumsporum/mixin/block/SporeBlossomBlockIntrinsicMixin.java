package com.axialeaa.florumsporum.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SporeBlossomBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;

@NullMarked
@Mixin(SporeBlossomBlock.class)
public class SporeBlossomBlockIntrinsicMixin implements BonemealableBlock {

    @Override
    @Intrinsic(displace = true)
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    @Intrinsic(displace = true)
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return false;
    }

    @Override
    @Intrinsic(displace = true)
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {}

}
