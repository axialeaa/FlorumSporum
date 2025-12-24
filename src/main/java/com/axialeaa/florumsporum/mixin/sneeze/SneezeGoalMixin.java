package com.axialeaa.florumsporum.mixin.sneeze;

import com.axialeaa.florumsporum.sneeze.PandaSneezeUtils;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.panda.Panda;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = "net.minecraft.world.entity.animal.panda.Panda$PandaSneezeGoal")
public abstract class SneezeGoalMixin extends GoalImplMixin {

    @Shadow @Final private Panda panda;

    @WrapMethod(method = "canUse")
    private boolean modifyCanUse(Operation<Boolean> original) {
        Level level = this.panda.level();
        BlockPos blockPos = BlockPos.containing(this.panda.getEyePosition());

        return level instanceof ServerLevel serverLevel && PandaSneezeUtils.canSneeze(serverLevel, this.panda, blockPos, level.getRandom());
    }

    @Override
    public boolean requiresUpdateEveryTickImpl(Operation<Boolean> original) {
        return true;
    }

}
