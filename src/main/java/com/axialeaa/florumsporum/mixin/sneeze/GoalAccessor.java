package com.axialeaa.florumsporum.mixin.sneeze;

import net.minecraft.world.entity.ai.goal.Goal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Goal.class)
public interface GoalAccessor {

    @Invoker("reducedTickDelay")
    static int invokeReducedTickDelay(int serverTicks) {
        throw new AssertionError();
    }

}
