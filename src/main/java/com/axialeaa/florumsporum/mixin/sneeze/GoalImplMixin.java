package com.axialeaa.florumsporum.mixin.sneeze;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.world.entity.ai.goal.Goal;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Goal.class)
public class GoalImplMixin {

    @WrapMethod(method = "requiresUpdateEveryTick")
    public boolean requiresUpdateEveryTickImpl(Operation<Boolean> original) {
        return original.call();
    }

}
