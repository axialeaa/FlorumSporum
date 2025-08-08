package com.axialeaa.florumsporum.mixin.sneeze;

import net.minecraft.entity.ai.goal.Goal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Goal.class)
public interface GoalAccessor {

    @Invoker("toGoalTicks")
    static int invokeToGoalTicks(int serverTicks) {
        throw new AssertionError();
    }

}
