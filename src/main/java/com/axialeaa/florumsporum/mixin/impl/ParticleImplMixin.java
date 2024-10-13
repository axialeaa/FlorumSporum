package com.axialeaa.florumsporum.mixin.impl;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Particle.class)
public class ParticleImplMixin {

    @WrapMethod(method = "move(DDD)V")
    public void moveImpl(double dx, double dy, double dz, Operation<Void> original) {
        original.call(dx, dy, dz);
    }

}
