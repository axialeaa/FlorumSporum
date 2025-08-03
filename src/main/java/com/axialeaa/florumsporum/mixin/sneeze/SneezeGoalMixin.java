package com.axialeaa.florumsporum.mixin.sneeze;

import com.axialeaa.florumsporum.block.SporeBlossomBehaviour;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.entity.passive.PandaEntity$SneezeGoal")
public abstract class SneezeGoalMixin extends Goal {

    @Shadow @Final private PandaEntity panda;

    @WrapMethod(method = "canStart")
    private boolean modifyCanStart(Operation<Boolean> original) {
        World world = this.panda.getWorld();
        BlockPos blockPos = BlockPos.ofFloored(this.panda.getEyePos());

        return SporeBlossomBehaviour.PandaSneeze.shouldSneeze(world, this.panda, blockPos, world.getRandom());
    }

}
